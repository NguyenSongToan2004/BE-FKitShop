package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Service.ChartService;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    ChartService chartService;
    @GetMapping("/product")
    public ResponseEntity<ResponseObject> getDataProduct() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get Data Product Successfully !!" , chartService.getProductData())
        );
    }
}
