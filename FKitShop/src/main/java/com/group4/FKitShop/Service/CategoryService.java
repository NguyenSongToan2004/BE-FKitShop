package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.CategoryMapper;
import com.group4.FKitShop.Repository.CategoryRepository;
import com.group4.FKitShop.Request.CategoryRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public List<Category> allCategory(){
        return categoryRepository.findAll();
    }

    public Category getCategoryByID(String id){
        Category cate = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Category_NOTFOUND));
        return cate;
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
            throw new AppException(ErrorCode.TagName_DUPLICATED);


        Category cate = categoryMapper.toCategory((request));
        cate.setCategoryID(generateUniqueCode());
//        category.setTagID(tagID);
//        category.setCategoryName(categoryName);
//        category.setDescription(description);


        return categoryRepository.save(cate);
    }

    public Category updateCategory(String id, CategoryRequest request){
        Category cate = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Category_NOTFOUND));

//        if(request.getTagID() != getCategoryByID(id).getTagID()) {
//            cate.setTagID(request.getTagID());
//        }
//
//        if(request.getCategoryName() == null){
//            cate.setCategoryName(getCategoryByID(id).getCategoryName());
//        }else{
//            cate.setCategoryName(request.getCategoryName());
//        }
//
//        if(request.getDescription() == null){
//            cate.setDescription(getCategoryByID(id).getDescription());
//        }else{
//            cate.setDescription(request.getDescription());
//        }

        cate.setTagID(request.getTagID());
        cate.setCategoryName(request.getCategoryName());
        cate.setDescription(request.getDescription());
        return categoryRepository.save(cate);
    }

    @Transactional
    public void deleteTag(String id) {
        if (!categoryRepository.existsById(id))
            throw new AppException(ErrorCode.Category_NOTFOUND);
        categoryRepository.deleteById(id);
    }


}
