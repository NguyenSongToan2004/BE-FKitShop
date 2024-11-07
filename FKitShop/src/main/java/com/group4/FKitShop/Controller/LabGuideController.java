package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.LabGuideRequest;
import com.group4.FKitShop.Request.LabGuideUpdateRequest;
import com.group4.FKitShop.Service.LabGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/lab-guide")
@CrossOrigin(origins = "http://localhost:5173")
public class LabGuideController {

    @Autowired
    private LabGuideService service;

//    @PostMapping("/")
////    ResponseEntity<ResponseObject> addLabGuide(
////            @RequestParam("labID") String labID,
////            @RequestParam("stepDescription") String stepDescription,
////            @RequestParam("image") MultipartFile image
////    ) {
////        LabGuideRequest request = new LabGuideRequest(labID, stepDescription);
////        return ResponseEntity.status(HttpStatus.CREATED).body(
////                new ResponseObject(1000, "Create new lab guide successfully !!", service.addLabGuide(request, image))
////        );
////    }

    @PreAuthorize("hasRole('admin') or hasRole('manager')")
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createLabGuide(@RequestBody LabGuideRequest labGuideRequest) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Create lab guide successfully !!", service.addLabGuide(labGuideRequest))
        );
    }

    @GetMapping("/guide/{guideID}")
    public ResponseEntity<ResponseObject> getGuideByID(@PathVariable int guideID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get a lab guide by labGuideID successfully !!", service.getGuideByID(guideID))
        );
    }

    @GetMapping("/guide-by-labID/{labID}")
    public ResponseEntity<ResponseObject> getLabGuidesByLabID(@PathVariable String labID ) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get lab guides by labID successfully !!", service.getLabGuidesByLabID(labID))
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAll() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get all lab guides successfully !!", service.getAll())
        );
    }

    @PreAuthorize("hasRole('admin') or hasRole('manager')")
    @PutMapping("/info/{guideID}")
    public ResponseEntity<ResponseObject> updateInfoLabGuide(@PathVariable int guideID, @RequestBody LabGuideRequest request) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Update a lab guide successfully !!", service.updateInfoLabGuide(guideID, request))
        );
    }

    @PreAuthorize("hasRole('admin') or hasRole('manager')")
    @DeleteMapping("/{guideID}")
    public ResponseEntity<ResponseObject> deleteLabGuide(@PathVariable int guideID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete a lab guide successfully !!", service.deleteLabGuide(guideID))
        );
    }
//    @GetMapping("/get/{id}")
//    ResponseEntity<ResponseObject> getOneLabGuide(@PathVariable int id) {
//        return ResponseEntity.ok(
//                new ResponseObject(1000, "Found Successfully !!", service.getLabGuide(id))
//        );
//    }
//
//    @GetMapping("/{id}")
//    ResponseEntity<ResponseObject> getLabGuides(@PathVariable String id) {
//        return ResponseEntity.ok(
//                new ResponseObject(1000, "Found Successfully !!", service.getLabGuides(id))
//        );
//    }
//
//    @DeleteMapping("{id}")
//    ResponseEntity<ResponseObject> deleteLabGuide(@PathVariable int id) {
//        return ResponseEntity.ok(
//                new ResponseObject(1000, "Delete Successfully !!", service.deleteLabGuide(id))
//        );
//    }
//
//    @PutMapping("/{id}")
//    ResponseEntity<ResponseObject> updateLabGuide(
//            @PathVariable int id,
//            @RequestParam("labID") String labID,
//            @RequestParam("stepDescription") String stepDescription,
//            @RequestParam("stepNumber") int stepNumber,
//            @RequestParam("image") MultipartFile image
//    ) {
//        LabGuideUpdateRequest request = new LabGuideUpdateRequest(labID, stepDescription, stepNumber);
//        return ResponseEntity.ok(
//                new ResponseObject(1000, "Update Lab Guide Successfully !!", service.updateLabGuide(request, image, id))
//        );
//    }
}
