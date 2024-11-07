package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.CateProduct;
import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.CategoryRequest;
import com.group4.FKitShop.Response.CategoryResponse;
import com.group4.FKitShop.Response.StringRespone;
import com.group4.FKitShop.Service.CateProductService;
import com.group4.FKitShop.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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

    // get cate active
    @GetMapping("/active")
    ResponseEntity<ResponseObject> getCateActive() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", categoryService.getCateActive())
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
    @GetMapping("/byTagID/{tagID}")
    ResponseEntity<ResponseObject> getCategoryByTag(@PathVariable int tagID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", categoryService.getCategoryByTag(tagID))
        );
    }

    // get cate by name contain
    @GetMapping("/byName/{name}")
    ResponseEntity<ResponseObject> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", categoryService.getCategoryByName(name))
        );
    }

    // create cate & create cateProduct relationship tuong ung
    @PreAuthorize("hasRole('admin') or hasRole('manager')")
    @PostMapping()
    public ResponseObject createCategory(@RequestBody @Valid CategoryRequest request) {
        //Category cate = categoryService.createCategory(request);
        //cateProductService.createCateProduct_Category(request);
        //List<CateProduct> cateProducts = cateProductService.getCateProductByCategoryID(cate.getCategoryID());
        return ResponseObject.builder()
                .status(1000)
                .message("Create category successfully")
                .data(categoryService.createCategory(request))
                .build();
    }

    @PreAuthorize("hasRole('admin') or hasRole('manager')")
    @PutMapping("/{categoryID}")
    public  ResponseObject updateCategory(@RequestBody @Valid CategoryRequest request, @PathVariable String categoryID) {
        //cateProductService.deleteCateProduct_Category(categoryID);
        //Category cate = categoryService.updateCategory(categoryID, request);
        //cateProductService.updateCateProduct_Category(categoryID, request);
        //List<CateProduct> cateProducts = cateProductService.getCateProductByCategoryID(cate.getCategoryID());

        return ResponseObject.builder()
                .status(1000)
                .message("Update category successfully")
                .data(categoryService.updateCategory(categoryID, request))
                .build();
    }

    // delete category
    @PreAuthorize("hasRole('admin') or hasRole('manager')")
    @DeleteMapping("/{categoryID}")
    ResponseEntity <ResponseObject> deleteCategory(@PathVariable String categoryID){
        //cateProductService.deleteCateProduct_Category(categoryID);
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete Successfully !!",  categoryService.deleteCategory(categoryID) + " row affeted")
        );
    }
}
