package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.ProductMapper;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.ProductRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProductMapper mapper;

    public Product addProduct(ProductRequest request, MultipartFile image) {
        if (repository.existsByName(request.getName()))
            throw new AppException(ErrorCode.PRODUCT_NAMEDUPLICATED);
        Product product = mapper.toProduct(request);
        product.setProductID(generateID());
        product.setCreateDate(new Date());
        product.setImage(uploadImage(image));
        return repository.save(product);
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );
    }

    public Product updateProduct(String id,
                                 String name, String description, String publisher,
                                 int quantity, double price, int discount,
                                 String status, double weight, String material,
                                 String dimension, String type, MultipartFile image) {

        Product product = repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );

        product.setName(name);
        product.setMaterial(material);
        product.setDiscount(discount);
        product.setPrice(price);
        product.setStatus(status);
        product.setType(type);
        product.setQuantity(quantity);
        product.setDimension(dimension);
        product.setPublisher(publisher);
        product.setWeight(weight);
        product.setDescription(description);
        String imageUrl = uploadImage(image);
        if(imageUrl != ""){
            product.setImage(imageUrl);
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

    public List<Product> getLastestProduct(){
        return repository.getLatestProduct();
    }

    String generateID() {
        String num = repository.getNumberProduct();
        int max = Integer.parseInt(num.substring(1, 6)) + 1;
        num = String.format("P%05d", max);
        return num;
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

    public List<Product> getActiveProduct(){
        return repository.getActiveProducts();
    }
}
