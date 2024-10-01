package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Lab;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.LabRequest;
import com.group4.FKitShop.Service.LabService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lab")
public class LabController {
    @Autowired
    private LabService labService;

    @PostMapping("/addLab")
    ResponseEntity<ResponseObject> addLab(@Valid @RequestBody LabRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Create Successfully !!", labService.addLabRequest(request))
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getLab(@PathVariable String id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Find Successfully !!", labService.getLab(id))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateLab(@PathVariable String id, @Valid @RequestBody LabRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Update Lab Sucessfully !!", labService.updateLab(id, request))
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteLab(@PathVariable String id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete Successfully !!", labService.deleteLab(id))
        );
    }

    @GetMapping("/labs")
    ResponseEntity<List<Lab>> getAllLabs() {
        return ResponseEntity.ok(labService.getAllLab());
    }

}