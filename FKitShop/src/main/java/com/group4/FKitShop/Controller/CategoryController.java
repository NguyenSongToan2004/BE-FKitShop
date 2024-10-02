package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Repository.CategoryRepository;
import com.group4.FKitShop.Request.CategoryRequest;
import com.group4.FKitShop.Request.CombineCateProductIDRequest;
import com.group4.FKitShop.Request.ProductIDRequest;
import com.group4.FKitShop.Service.CateProductService;
import com.group4.FKitShop.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;
    CateProductService cateProductService;

    @GetMapping()
    public List<Category> allCategory(){
        return categoryService.allCategory();
    }

    @GetMapping("/{categoryID}")
    ResponseEntity<ResponseObject> getCategoryByID(@PathVariable() String categoryID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", categoryService.getCategoryByID(categoryID))
        );
    }

//    @PostMapping()
//    public ResponseObject createCategory(@RequestBody @Valid CategoryRequest request,
//                                         @RequestBody ProductIDRequest productIDRequest) {
//
//
//
//        return ResponseObject.builder()
//                .status(1000)
//                .message("Create category successfully")
//                .data(categoryService.createCategory(request))
//                .data(cateProductService.createCateProduct_Category(productIDRequest))
//                .build();
//    }

    @PostMapping()
    public ResponseObject createCategory(@RequestBody @Valid CategoryRequest request) {

        categoryService.createCategory(request);
        cateProductService.createCateProduct_Category(request);

        return ResponseObject.builder()
                .status(1000)
                .message("Create category successfully")
                .build();
    }

    @PutMapping("/{categoryID}")
    public  ResponseObject updateCategory(@RequestBody @Valid CategoryRequest request, @PathVariable String categoryID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update tag successfully")
                .data(categoryService.updateCategory(categoryID, request))
                .build();
    }

    @DeleteMapping("/{tagID}")
    public ResponseObject deleteCategory(@PathVariable String categoryID){
        categoryService.deleteTag(categoryID);
        return ResponseObject.builder()
                .status(1000)
                .message("Delete tag successfully")
                .build();

    }
}
