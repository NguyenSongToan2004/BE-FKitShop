package com.group4.FKitShop.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.ServiceFeeRequest;
import com.group4.FKitShop.Service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
public class DeliverController {

    @Autowired
    private DeliveryService service;

    @GetMapping("/province-city")
    ResponseEntity<JsonNode> getProvinceCity() {
        return service.getProvinces();
    }

    @GetMapping("/district/{provinceID}")
    ResponseEntity<JsonNode> getDistrict(@PathVariable("provinceID") int provinceID) {
        return service.getDistrict(provinceID);
    }

    @GetMapping("/ward/{districtID}")
    public ResponseEntity<JsonNode> getWard(@PathVariable("districtID") int districtID) {
        return service.getWard(districtID);
    }

    @PostMapping("/service-fee")
    public ResponseEntity<JsonNode> serviceFee(@RequestBody ServiceFeeRequest request) {
        return service.calculateServicePrice(request);
    }
}
