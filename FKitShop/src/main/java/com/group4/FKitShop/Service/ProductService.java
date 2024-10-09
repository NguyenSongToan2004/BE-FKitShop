package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Image;
import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.File.FileManagement;
import com.group4.FKitShop.Mapper.ProductMapper;
import com.group4.FKitShop.Repository.ImageRepository;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.ProductRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    //    @Value("${upload.directory-upload.product}")
    //    private String uploadDirectory;
    @Autowired
    private ProductRepository repository;
    @Value("${amazonProperties.folder.Product}")
    private String uploadDirectory;
    @Autowired
    private AmazonClient amazonClient;
    @Autowired
    private ImageRepository imageRepository;

    public Product addProduct(ProductRequest request, MultipartFile[] images) {
        if (repository.existsByName(request.getName()))
            throw new AppException(ErrorCode.PRODUCT_NAMEDUPLICATED);
        Product product = new Product();
        product.setProductID(generateID());
        product.setCreateDate(new Date());
        product.setImages(new ArrayList<Image>());
        for (MultipartFile file : images) {
            Image image = new Image();
            image.setName(file.getOriginalFilename());
            String imgUrl = existFileName(file.getOriginalFilename());
            if (imgUrl != null)
                image.setUrl(imgUrl);
            else
                image.setUrl(amazonClient.uploadFile((MultipartFile) file, uploadDirectory));
            image.setProduct(product);
            product.getImages().add(image);
        }
        ProductMapper.INSTANCE.toProduct(request, product);

        return repository.save(product);
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );
    }

    public Product updateProduct(String id, ProductRequest request) {

        Product product = repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );

        ProductMapper.INSTANCE.toProduct(request, product);
        return repository.save(product);
    }

    public String updateImage(MultipartFile image, String productID, int imageID) {
        if (image.isEmpty()) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
        Product product = repository.findById(productID).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );
        for (Image i : product.getImages()) {
            if (i.getId() == imageID) {
                String url = existFileName(image.getOriginalFilename());
                if (url == null)
                    i.setUrl(amazonClient.uploadFile(image, uploadDirectory));
                else
                    i.setUrl(url);
                i.setName(image.getOriginalFilename());
                repository.save(product);
                break;
            }
        }
        return image.getOriginalFilename();
    }

    public Product addImages(MultipartFile[] images, String productID) {
        if (images.length == 0) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
        Product product = repository.findById(productID).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );

        for (MultipartFile file : images) {
            if(repository.existsByUrl(productID, "%"+file.getOriginalFilename()) == null){
                String url = existFileName(file.getOriginalFilename());
                Image image = new Image();
                if(url == null)
                    image.setUrl(amazonClient.uploadFile(file, uploadDirectory));
                else
                    image.setUrl(url);
                image.setName(file.getOriginalFilename());
                image.setProduct(product);
                product.getImages().add(image);
            }
        }
        return repository.save(product);
    }

    @Transactional
    public int deleteProduct(String id) {
        if (!repository.existsById(id))
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        return repository.deleteStatus(id);
    }

    public List<Product> getAllProduct() {
        return repository.findAll();
    }

    public List<Product> getLastestProduct() {
        return repository.getLatestProduct();
    }

    String generateID() {
        String num = repository.getNumberProduct();
        if (num == null) {
            return String.format("P%05d", 1);
        }
        int max = Integer.parseInt(num.substring(1, 6)) + 1;
        num = String.format("P%05d", max);
        return num;
    }

    private String existFileName(String fileName) {
        String imgUrl = imageRepository.existFileName("%" + fileName + "%");
        return imgUrl;
    }

    public List<Product> getHotProduct(){
        return repository.getHotProducts();
    }

    public List<Product> getPriceAscProducts() {
        return repository.getPriceAscProducts();
    }

    public List<Product> getPriceDescProducts() {
        return repository.getPriceDescProducts();
    }

    private static final String UPLOAD_DIRECTORY = "uploads" + File.separator + "products";

    String uploadImage(MultipartFile file) {
        // Kiểm tra xem file có rỗng không
        if (file.isEmpty()) {
            return "";
        }
        try {
            // Lấy đường dẫn tương đối đến thư mục uploads (có thể thay đổi tùy môi trường)
            String uploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIRECTORY;
            System.out.println("upload dir : "+ uploadDir);
            // System.getProperty("user.dir") : lấy ra đường dẫn đến thư mục hiện tại
            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            // Lưu file vào thư mục
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return UPLOAD_DIRECTORY + File.separator + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    public List<Product> getActiveProduct() {
        return repository.getActiveProducts();
    }

    public Product updateQuantity(Integer quantity, String id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );
        product.setQuantity(quantity);
        return repository.save(product);
    }

    //    //private static final String UPLOAD_DIRECTORY = "FKitShop" +File.separator+ "src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"uploads";
//    @Value("${upload.directory}")
//    private String UPLOAD_DIRECTORY;
//
//    String uploadImage(MultipartFile file, String directoryUpload) {
//        // Kiểm tra xem file có rỗng không
//        if (file.isEmpty()) {
//            return "";
//        }
//        try {
//            // Lấy đường dẫn tương đối đến thư mục uploads (có thể thay đổi tùy môi trường)
//            String uploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIRECTORY + File.separator+ "products";
//            // lấy ra đường dẫn đến thư mục hiện tại
//            System.out.println(System.getProperty("user.dir"));// Tạo thư mục nếu chưa tồn tại
//            File directory = new File(uploadDir);
//            if (!directory.exists()) {
//                directory.mkdir();
//            }
//            // Lưu file vào thư mục
//            String filePath = uploadDir + File.separator + file.getOriginalFilename();
//            file.transferTo(new File(filePath));
//            return UPLOAD_DIRECTORY + File.separator + file.getOriginalFilename();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
//        }
//    }

    //    public Product updateProduct(String id, ProductRequest request, MultipartFile[] image) {
//
//        Product product = repository.findById(id).orElseThrow(
//                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
//        );
//
//        for (Image imageProduct : product.getImages()){
//            String imageUrl = amazonClient.uploadFile(image, uploadDirectory);
//            if(imageUrl != ""){
//                product.setImage(imageUrl);
//            }
//        }
//
//        ProductMapper.INSTANCE.toProduct(request,product);
//        return repository.save(product);
//    }
    

}
