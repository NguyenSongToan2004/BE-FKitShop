package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.CateProduct;
import com.group4.FKitShop.Request.ProductIDRequest;
import com.group4.FKitShop.Service.CateProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cateproducts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CateProductController {

    CateProductService cateProductService;

    @GetMapping()
    public List<CateProduct> getAllCateProducts() {
        return cateProductService.getAllCateProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CateProduct> getCateProductById(@PathVariable Integer id) {
        CateProduct cateProduct = cateProductService.getCateProductById(id);
        return ResponseEntity.ok(cateProduct);
    }

//    @PostMapping()
//    public CateProduct createCateProduct(@RequestBody CateProduct cateProduct, ProductIDRequest productIDRequest) {
//        return cateProductService.createCateProduct(cateProduct);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<CateProduct> updateCateProduct(@PathVariable Integer id, @RequestBody CateProduct cateProduct) {
        CateProduct updatedCateProduct = cateProductService.updateCateProduct(id, cateProduct);
        return ResponseEntity.ok(updatedCateProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCateProduct(@PathVariable Integer id) {
        cateProductService.deleteCateProduct(id);
        return ResponseEntity.noContent().build();
    }
}
