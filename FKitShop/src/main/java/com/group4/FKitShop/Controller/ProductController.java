package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.ProductRequest;
import com.group4.FKitShop.Service.ProductService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService service;

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
            @RequestParam("image") MultipartFile image
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
                .build();
        Product product = service.addProduct(request, image);
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

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> getProduct(@PathVariable String id,
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
                                              @RequestParam("image") MultipartFile image) {
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
                .build();
        Product product = service.updateProduct(id, request, image);
        return ResponseEntity.ok(
                new ResponseObject(1000, "Update Successfully !!", product)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete Successfully !!", service.deleteProduct(id) + " row affeted")
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