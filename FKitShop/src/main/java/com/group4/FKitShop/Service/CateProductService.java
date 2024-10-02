package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.CateProduct;
import com.group4.FKitShop.Repository.CateProductRepository;
import com.group4.FKitShop.Repository.CategoryRepository;
import com.group4.FKitShop.Request.CategoryRequest;
import com.group4.FKitShop.Request.ProductIDRequest;
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
    CateProductRepository cateProductRepository;

    public List<CateProduct> getAllCateProducts() {
        return cateProductRepository.findAll();
    }

    public CateProduct getCateProductById(Integer id) {
        return cateProductRepository.findById(id).orElse(null);
    }

    public String generateCategoryID() {
        int number = 1;
        String code;

        do {
            code = String.format("C%05d", number);
            number++;
        } while (categoryRepository.existsById(code));
        String id = String.format("C%05d", number - 1);
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

    public CateProduct updateCateProduct(int id, CateProduct cateProduct) {
        if (cateProductRepository.existsById(id)) {
            cateProduct.setCateProductID(id);
            return cateProductRepository.save(cateProduct);
        }
        return null; // Or throw an exception
    }

    public void deleteCateProduct(Integer id) {
        cateProductRepository.deleteById(id);
    }
}

