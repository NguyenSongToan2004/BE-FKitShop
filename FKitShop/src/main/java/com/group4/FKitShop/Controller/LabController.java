package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Lab;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Request.CreateFilePDFRequest;
import com.group4.FKitShop.Request.DownloadLabRequest;
import com.group4.FKitShop.Request.LabRequest;
import com.group4.FKitShop.Response.CKEditorResponse;
import com.group4.FKitShop.Service.AmazonClient;
import com.group4.FKitShop.Service.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/lab")
@CrossOrigin(origins = "http://localhost:5173")
public class LabController {
    @Autowired
    private LabService labService;
    @Autowired
    private AmazonClient amazonClient;
//    String productID;
//    String name;
//    String description;
//    String level;
    @PostMapping("/addLab")
    ResponseEntity<ResponseObject> addLab(
            @RequestParam("productID") String productID,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("level") String level,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        LabRequest request = new LabRequest(productID, name, description, level);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Create Successfully !!", labService.addLabRequest(request, file))
        );
    }

    @PostMapping("/pdf/create/{labID}")
    ResponseEntity<ResponseObject> createFilePDF(@PathVariable("labID") String labID,
                                                 @RequestBody CreateFilePDFRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Create Successfully !!",
                        labService.createFilePDF(labID, request))
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getLab(@PathVariable String id) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Find Successfully !!", labService.getLab(id))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateLab(
            @PathVariable String id,
            @RequestParam("productID") String productID,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("level") String level,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        LabRequest request = new LabRequest(productID, name, description, level);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Update Lab Sucessfully !!", labService.updateLab(id, request, file))
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

    @PostMapping("/upload-lab/{labID}")
    ResponseEntity<ResponseObject> uploadLab(@RequestParam("file") MultipartFile file, @PathVariable("labID") String id) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ResponseObject.builder()
                            .status(1000)
                            .message("Upload Successfully !!")
                            .data(labService.saveLabPDF(file, id))
                            .build()
            );
    }

    @GetMapping("/get-link-download")
    ResponseEntity<ResponseObject> getLinkDownLoad(@RequestParam("accountID") String accountID,
                                         @RequestParam("orderID") String orderID,
                                         @RequestParam("labID") String labID,
                                         @RequestParam("productID") String productID,
                                         @RequestParam("fileName") String fileName) {
        String link = String.format("http://localhost:8080/fkshop/lab/download/accountID=%s/" +
                "orderID=%s/labID=%s/productID=%s/fileName=%s", accountID, orderID, labID, productID, fileName);
        return ResponseEntity.ok(
                new ResponseObject(1000, "Download Successfully !!", link)
        );
    }

    @GetMapping("/download")
    ResponseEntity<Resource> downloadLab(@RequestParam("accountID") String accountID,
                                         @RequestParam("orderID") String orderID,
                                         @RequestParam("labID") String labID,
                                         @RequestParam("productID") String productID,
                                         @RequestParam("fileName") String fileName
                                         ) {
        DownloadLabRequest request = new DownloadLabRequest(accountID, orderID, labID, productID, fileName);
        try {
            var fileToDownload = labService.downloadFilePDF(request);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                            request.getFileName() + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(fileToDownload));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.LAB_DOWNLOAD_FAILED);
        }
    }

    @GetMapping("/account/{accountID}")
    ResponseEntity<ResponseObject> getLabByAccountID(@PathVariable String accountID) {
        return  ResponseEntity.ok(
                new ResponseObject(1000, "Get labs successfully !!", labService.getLabByAccountID(accountID))
        );
    }

    @GetMapping("/product/{productID}")
    ResponseEntity<ResponseObject> getLabByProductID(@PathVariable String productID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get lab successfully !!", labService.getLabByProductID(productID))
        );
    }

    @PostMapping("/upload-img")
    ResponseEntity<CKEditorResponse> uploadImg(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new CKEditorResponse(true, amazonClient.uploadFile(file, "Lab-img"))
        );
    }
}