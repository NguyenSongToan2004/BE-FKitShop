package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.LabGuideRequest;
import com.group4.FKitShop.Request.LabGuideUpdateRequest;
import com.group4.FKitShop.Service.LabGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/labguide")
@CrossOrigin(origins = "http://localhost:5173")
public class LabGuideController {

    @Autowired
    private LabGuideService service;

    @PostMapping("/")
    ResponseEntity<ResponseObject> addLabGuide(
            @RequestParam("labID") String labID,
            @RequestParam("stepDescription") String stepDescription,
            @RequestParam("image") MultipartFile image
    ) {
        LabGuideRequest request = new LabGuideRequest(labID, stepDescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(1000, "Create new lab guide successfully !!", service.addLabGuide(request, image))
        );
    }

    @GetMapping("/get/{id}")
    ResponseEntity<ResponseObject> getOneLabGuide(@PathVariable int id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found Successfully !!", service.getLabGuide(id))
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getLabGuides(@PathVariable String id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found Successfully !!", service.getLabGuides(id))
        );
    }

    @DeleteMapping("{id}")
    ResponseEntity<ResponseObject> deleteLabGuide(@PathVariable int id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete Successfully !!", service.deleteLabGuide(id))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateLabGuide(
            @PathVariable int id,
            @RequestParam("labID") String labID,
            @RequestParam("stepDescription") String stepDescription,
            @RequestParam("stepNumber") int stepNumber,
            @RequestParam("image") MultipartFile image
    ) {
        LabGuideUpdateRequest request = new LabGuideUpdateRequest(labID, stepDescription, stepNumber);
        return ResponseEntity.ok(
                new ResponseObject(1000, "Update Lab Guide Successfully !!", service.updateLabGuide(request, image, id))
        );
    }
}
