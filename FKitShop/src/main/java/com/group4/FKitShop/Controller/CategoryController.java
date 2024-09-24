package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Request.CreateCategoryRequest;
import com.group4.FKitShop.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public List<Category> allCategory(){
        return categoryService.allCategory();
    }

    @PostMapping()
    Category createCategory(@RequestBody CreateCategoryRequest request){
        return categoryService.createCategory(request);
    }
}
