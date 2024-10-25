package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/storage/")
public class BucketController {

    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

//    @PostMapping("/uploadFile")
//    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
//        return this.amazonClient.uploadFile(file);
//    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file,
                             @RequestParam(value = "folderName") String folderName) { // Add folderName parameter
        return this.amazonClient.uploadFile(file, folderName); // Pass folderName to the service
    }

    @PostMapping("/uploadMultiFile")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files,
                                                    @RequestParam(value = "folderName") String folderName) {
        List<String> uploadedFileUrls = this.amazonClient.uploadFiles(files, folderName);
        return ResponseEntity.ok(uploadedFileUrls);
    }

}