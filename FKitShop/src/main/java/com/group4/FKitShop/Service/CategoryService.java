package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Repository.CategoryRepository;
import com.group4.FKitShop.Request.CreateCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CategoryService {

    @Autowired
    private com.group4.FKitShop.Repository.CategoryRepository categoryRepository;

    public List<Category> allCategory(){
        return categoryRepository.findAll();
    }

    public Category createCategory(CreateCategoryRequest request){
        Category category = new Category();

        Random random = new Random();
        int number = random.nextInt(1000000); // Generates a number between 0 and 999999
        String strCategoryID = String.format("C%06d", number); // Formats the number as C + 6 digits
        category.setCategoryID(strCategoryID);
        category.setTagID(request.getTagID());
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        return categoryRepository.save(category);
    }
}
