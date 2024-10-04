package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.CategoryRequest;
import com.group4.FKitShop.Response.StringRespone;
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

    // get all cate
    @GetMapping()
    public List<Category> allCategory(){
        return categoryService.allCategory();
    }


    // get cate by ID
    @GetMapping("/{categoryID}")
    ResponseEntity<ResponseObject> getCategoryByID(@PathVariable() String categoryID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", categoryService.getCategoryByID(categoryID))
        );
    }

    // get list category by productID
    @GetMapping("/byProductID/{productID}")
    ResponseEntity<ResponseObject> getCategoryByProductID(@PathVariable String productID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", categoryService.getCategoryList(productID))
        );
    }

    // get cate by tagID
    @GetMapping("/bytagID/{tagID}")
    ResponseEntity<ResponseObject> getCategoryByTag(@PathVariable String tagID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", categoryService.getCategoryByTag(tagID))
        );
    }

    // create cate & create cateProduct relationship tuong ung
    @PostMapping()
    public ResponseObject createCategory(@RequestBody @Valid CategoryRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create category successfully")
                .data(categoryService.createCategory(request))
                .data(cateProductService.createCateProduct_Category(request))
                .build();
    }

    @PutMapping("/{categoryID}")
    public  ResponseObject updateCategory(@RequestBody @Valid CategoryRequest request, @PathVariable String categoryID) {
        cateProductService.deleteCateProduct_Category(categoryID);
        return ResponseObject.builder()
                .status(1000)
                .message("Update category successfully")
                .data(categoryService.updateCategory(categoryID, request))
                .data(cateProductService.createCateProduct_Category(request))
                .build();
    }

    // delete category & delete cateProduct tuong ung
    @DeleteMapping("/{categoryID}")
    public ResponseObject deleteCategory(@PathVariable String categoryID){
        categoryService.deleteCategory(categoryID);
        cateProductService.deleteCateProduct_Category(categoryID);
        return ResponseObject.builder()
                .status(1000)
                .message("Delete category successfully")
                .build();
    }
}
