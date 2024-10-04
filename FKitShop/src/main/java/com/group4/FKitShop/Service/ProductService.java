package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.File.FileManagement;
import com.group4.FKitShop.Mapper.ProductMapper;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.ProductRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Value("${upload.directory-upload.product}")
    private String uploadDirectory;

    public Product addProduct(ProductRequest request, MultipartFile image) {
        if (repository.existsByName(request.getName()))
            throw new AppException(ErrorCode.PRODUCT_NAMEDUPLICATED);
        Product product = new Product();
        product.setProductID(generateID());
        product.setCreateDate(new Date());
        product.setImage(FileManagement.uploadImage(image, uploadDirectory));
        ProductMapper.INSTANCE.toProduct(request, product);

        return repository.save(product);
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );
    }

    public Product updateProduct(String id, ProductRequest request, MultipartFile image) {

        Product product = repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );
        String imageUrl = FileManagement.uploadImage(image, uploadDirectory);
        if(imageUrl != ""){
            product.setImage(imageUrl);
        }
        ProductMapper.INSTANCE.toProduct(request,product);
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

    public List<Product> getLastestProduct(){
        return repository.getLatestProduct();
    }

    String generateID() {
        String num = repository.getNumberProduct();
        int max = Integer.parseInt(num.substring(1, 6)) + 1;
        num = String.format("P%05d", max);
        return num;
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

    public List<Product> getActiveProduct(){
        return repository.getActiveProducts();
    }
}
