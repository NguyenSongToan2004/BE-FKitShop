package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.CateProduct;
import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.ProductRequest;
import com.group4.FKitShop.Response.CategoryResponse;
import com.group4.FKitShop.Response.ProductResponse;
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
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    ProductService service;
    @Autowired
    CateProductService cateProductService;
    @Autowired
    CategoryService categoryService;

    // create product & cateProduct relation tuong ung
    @PostMapping("/add")
    public ResponseObject addProduct(
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
            @RequestParam("images") MultipartFile[] image,
            @RequestParam("categoryID") List<String> categoryID
    ) {
        System.out.println("tới đây");
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
        List<CateProduct> cateProducts = cateProductService.getCateProductByProductID(product.getProductID());

        return ResponseObject.builder()
                .status(1000)
                .message("Create product successfully")
                .data(new ProductResponse(product, cateProducts))
                .build();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found Successfully", service.getProduct(id))
        );
    }

    @PutMapping("/{productID}")
    public ResponseObject getProduct(@PathVariable String productID,
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
        Product product = service.updateProduct(productID, request);

        cateProductService.deleteCateProduct_Product(productID);
        cateProductService.updateCateProduct_Product(productID, request);
        List<CateProduct> cateProducts = cateProductService.getCateProductByProductID(product.getProductID());

        return ResponseObject.builder()
                .status(1000)
                .message("Update product successfully")
                .data(new ProductResponse(product, cateProducts))
                .build();
    }

    // delete product
    @DeleteMapping("/{productID}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable String productID) {
        //cateProductService.deleteCateProduct_Product(productID);
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete Successfully !!", service.deleteProduct(productID) + " row affeted")
        );
    }

    @PutMapping("/image/{productID}/{imageID}")
    ResponseEntity<ResponseObject> updateImage(@RequestParam("image") MultipartFile image, @PathVariable String productID, @PathVariable int imageID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Update image successfully !!", service.updateImage(image, productID, imageID) + " row affeted")
        );
    }
    // get list product by categoryID
    @GetMapping("/byCategoryID/{categoryID}")
    ResponseEntity<ResponseObject> getProductByCategoryID(@PathVariable String categoryID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", service.getProductIDList(categoryID))
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

    @GetMapping("/hot")
    ResponseEntity<ResponseObject> getHotProducts() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get Hot Products Successfully !!", service.getHotProduct())
        );
    }

    @GetMapping("/price-asc")
    ResponseEntity<ResponseObject> getPriceAscProducts() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get products by asc price successfully !!", service.getPriceAscProducts())
        );
    }

    @GetMapping("/price-desc")
    ResponseEntity<ResponseObject> getPriceDescProducts(){
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get products by asc price successfully !!", service.getPriceDescProducts())
        );
    }

    @PutMapping("/add-images/{productID}")
    ResponseEntity<ResponseObject> addImages(@RequestParam("images") MultipartFile[] images, @PathVariable("productID") String productID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Upload images successully !!", service.addImages(images, productID))
        );
    }


}
