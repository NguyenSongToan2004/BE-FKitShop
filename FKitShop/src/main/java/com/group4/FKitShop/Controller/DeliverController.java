package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliverController {

    @Autowired
    private DeliveryService service;

    @ResponseBody
    @GetMapping("/province-city")
    ResponseEntity<ResponseObject> getProvinceCity() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get all province and service successfully !!",
                        service.getProvinceOrderCity())
        );
    }
}
