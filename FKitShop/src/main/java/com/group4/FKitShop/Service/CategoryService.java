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
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));
        return cate;
    }

    // get cate by productid
    public List<Category> getCategoryByProductID(String productID){
        List<CateProduct> cateProductList  = cateProductRepository.findByProductID(productID);
        List<Category> categoryList = new ArrayList<>();
        for( CateProduct cateProduct : cateProductList ){
            Category category = getCategoryByID(cateProduct.getCategoryID());
            categoryList.add(category);
        }
        return categoryList;
    }

    // get cate by name contain
    public List<Category> getCategoryByName(String name){
        return categoryRepository.getCategoryByName("%"+name+"%");
    }

    // list cate by productID
    public List<Category> getCategoryList(String id){
        return categoryRepository.getCategoryList(id);
    }

    // list cate by tagID
    public List<Category> getCategoryByTag(int id){
        return categoryRepository.getCategoryByTagID(id);
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

    public Category createCategory(CategoryRequest
                                           request){
        if (categoryRepository.existsByCategoryName(request.getCategoryName()))
            throw new AppException(ErrorCode.CATEGORY_NAME_DUPLICATED);

        Category cate = categoryMapper.toCategory((request));
      //  Category cate = new Category();
        cate.setCategoryID(generateUniqueCode());
        cate.setStatus(1);
//        cate.setTagID(request.getTagID());
//        cate.setCategoryName(request.getCategoryName());
//        cate.setDescription(request.getDescription());
        return categoryRepository.save(cate);
    }

    public Category updateCategory(String id, CategoryRequest request){
        Category cate = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));

        cate.setTagID(request.getTagID());
        cate.setCategoryName(request.getCategoryName());
        cate.setDescription(request.getDescription());
        cate.setStatus(request.getStatus());
        return categoryRepository.save(cate);
    }

    @Transactional
    public int deleteCategory(String id) {
        if (!categoryRepository.existsById(id))
            throw new AppException(ErrorCode.CATEGORY_NOTFOUND);
        return categoryRepository.deleteStatus(id);
    }

}
