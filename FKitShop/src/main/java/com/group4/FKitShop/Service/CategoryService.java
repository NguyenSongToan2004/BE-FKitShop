package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.CateProduct;
import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.CateProductRepository;
import com.group4.FKitShop.Repository.CategoryRepository;
import com.group4.FKitShop.Request.CategoryRequest;
import com.group4.FKitShop.Mapper.CategoryMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CateProductRepository cateProductRepository;
    CategoryMapper categoryMapper;

    public List<Category> allCategory(){
        return categoryRepository.findAll();
    }

    public Category getCategoryByID(String id){
        Category cate = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Category_NOTFOUND));
        return cate;
    }

    public List<Category> getCategoryByProductID(String productID){
        List<CateProduct> cateProductList  = cateProductRepository.findByProductID(productID);
        List<Category> categoryList = new ArrayList<>();
        for( CateProduct cateProduct : cateProductList ){
            Category category = getCategoryByID(cateProduct.getCategoryID());
            categoryList.add(category);
        }
        return categoryList;
    }

    public String generateUniqueCode() {
        int number = 1;
        String code;

        do {
            code = String.format("C%05d", number);
            number++;
        } while (categoryRepository.existsById(code));
        return code;
    }

    public Category createCategory(CategoryRequest request){
        if (categoryRepository.existsByCategoryName(request.getCategoryName()))
            throw new AppException(ErrorCode.CategoryName_DUPLICATED);

      //  Category cate = categoryMapper.toCategory((request));
        Category cate = new Category();
        cate.setCategoryID(generateUniqueCode());
        cate.setTagID(request.getTagID());
        cate.setCategoryName(request.getCategoryName());
        cate.setDescription(request.getDescription());


        return categoryRepository.save(cate);
    }

    public Category updateCategory(String id, CategoryRequest request){
        Category cate = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Category_NOTFOUND));

        cate.setTagID(request.getTagID());
        cate.setCategoryName(request.getCategoryName());
        cate.setDescription(request.getDescription());
        return categoryRepository.save(cate);
    }

    @Transactional
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id))
            throw new AppException(ErrorCode.Category_NOTFOUND);
        categoryRepository.deleteById(id);
    }

    // list cate by productID
    public List<Category> getCategoryList(String id){
        return categoryRepository.getCategoryList(id);
    }

    // list cate by tagID
    public List<Category> getCategoryByTag(String id){
        return categoryRepository.getCategoryByTagID(id);
    }


}
