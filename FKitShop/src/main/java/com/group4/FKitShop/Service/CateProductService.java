package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.CateProduct;
import com.group4.FKitShop.Repository.CateProductRepository;
import com.group4.FKitShop.Repository.CategoryRepository;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.CategoryRequest;
import com.group4.FKitShop.Request.ProductRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CateProductService {

    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    CateProductRepository cateProductRepository;

    public List<CateProduct> getAllCateProducts() {
        return cateProductRepository.findAll();
    }

    public List<CateProduct> getCateProductByCategoryID(String id) {
        return cateProductRepository.findByCategoryID(id);
    }

    public List<CateProduct> getCateProductByProductID(String id) {
        return cateProductRepository.findByProductID(id);
    }

    public String generateCategoryID() {
        int number = 1;
        String code;

        do {
            code = String.format("C%05d", number);
            number++;
        } while (categoryRepository.existsById(code));
        String id = String.format("C%05d", number - 2);
        return id;
    }

    public boolean createCateProduct_Category(CategoryRequest request) {
        List<String> productIDs = request.getProductID();

        String categoryID = generateCategoryID();

        // Loop through all product IDs
        for (String productID : productIDs) {
            CateProduct cateProduct = new CateProduct();
            cateProduct.setCategoryID(categoryID);
            cateProduct.setProductID(productID);
            cateProductRepository.save(cateProduct);
        }
        return true;
    }

    public void deleteCateProduct_Category(String categoryID) {
        List<CateProduct> cateProducts = cateProductRepository.findByCategoryID(categoryID);
        for (CateProduct obj : cateProducts) {
            cateProductRepository.deleteById(obj.getCateProductID());
        }
    }

    public String generateProductID() {
        int number = 1;
        String code;

        do {
            code = String.format("P%05d", number);
            number++;
        } while (productRepository.existsById(code));
        String id = String.format("P%05d", number - 2);
        return id;
    }

    public void createCateProduct_Product(ProductRequest request) {
        List<String> categoryIDs = request.getCategoryID();

        String productID = generateProductID();

        for (String categoryID : categoryIDs) {
            CateProduct cateProduct = new CateProduct();
            cateProduct.setCategoryID(categoryID);
            cateProduct.setProductID(productID);
            cateProductRepository.save(cateProduct);
        }
    }

    public void deleteCateProduct_Product(String productID) {
        List<CateProduct> cateProducts = cateProductRepository.findByProductID(productID);
        for (CateProduct obj : cateProducts) {
            cateProductRepository.deleteById(obj.getCateProductID());
        }
    }
}

