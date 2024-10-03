package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.ProductRequest;
import com.group4.FKitShop.Response.StringRespone;
import com.group4.FKitShop.Service.CateProductService;
import com.group4.FKitShop.Service.CategoryService;
import com.group4.FKitShop.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")

public class ProductController {

    @Autowired
    ProductService service;
    @Autowired
    CateProductService cateProductService;
    @Autowired
    CategoryService categoryService;

    @PostMapping("/")
    ResponseEntity<ResponseObject> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("publisher") String publisher,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") double price,
            @RequestParam("discount") int discount,
            @RequestParam("status") String status,
            @RequestParam("weight") double weight,
            @RequestParam("material") String material,
            @RequestParam("dimension") String dimension,
            @RequestParam("type") String type,
            @RequestParam("image") MultipartFile image,
            @RequestParam("categoryID") List<String> categoryID
    ) {
        ProductRequest request = ProductRequest.builder()
                .name(name)
                .description(description)
                .publisher(publisher)
                .quantity(quantity)
                .price(price)
                .discount(discount)
                .status(status)
                .weight(weight)
                .material(material)
                .dimension(dimension)
                .type(type)
                .categoryID(categoryID)
                .build();
        Product product = service.addProduct(request, image);
        cateProductService.createCateProduct_Product(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(1000, "Create new product sucessfully !!", product)
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found Successfully", service.getProduct(id))
        );
    }

    @PutMapping("/{productID}")
    ResponseEntity<ResponseObject> getProduct(@PathVariable String productID,
                                              @RequestParam("name") String name,
                                              @RequestParam("description") String description,
                                              @RequestParam("publisher") String publisher,
                                              @RequestParam("quantity") int quantity,
                                              @RequestParam("price") double price,
                                              @RequestParam("discount") int discount,
                                              @RequestParam("status") String status,
                                              @RequestParam("weight") double weight,
                                              @RequestParam("material") String material,
                                              @RequestParam("dimension") String dimension,
                                              @RequestParam("type") String type,
                                              @RequestParam("image") MultipartFile image,
                                              @RequestParam("categoryID") List<String> categoryID) {
        ProductRequest request = ProductRequest.builder()
                .name(name)
                .description(description)
                .publisher(publisher)
                .quantity(quantity)
                .price(price)
                .discount(discount)
                .status(status)
                .weight(weight)
                .material(material)
                .dimension(dimension)
                .type(type)
                .categoryID(categoryID)
                .build();
        Product product = service.updateProduct(productID, request, image);
        cateProductService.deleteCateProduct_Product(productID);
        cateProductService.createCateProduct_Product(request);
        return ResponseEntity.ok(
                new ResponseObject(1000, "Update Successfully !!", product)
        );
    }

    @DeleteMapping("/{productID}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable String productID) {
        cateProductService.deleteCateProduct_Product(productID);
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete Successfully !!", service.deleteProduct(productID) + " row affeted")
        );
    }

    // get list product by categoryID
    @GetMapping("/byCategoryID/{categoryID}")
    ResponseEntity<StringRespone> getProductByCategoryID(@PathVariable String categoryID) {
        return ResponseEntity.ok(
                new StringRespone(1000, "Found successfully", service.getProductIDList(categoryID))
        );
    }

    @GetMapping("/products")
    ResponseEntity<ResponseObject> getALlProduct() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get ALl Product Successfully", service.getAllProduct())
        );
    }

    @GetMapping("/latest")
    ResponseEntity<ResponseObject> getLatestProduct() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Successfully !!", service.getLastestProduct())
        );
    }

    @GetMapping("/aproducts")
    ResponseEntity<ResponseObject> getActiveProducts() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get Products Successfully !!", service.getActiveProduct())
        );
    }
}
