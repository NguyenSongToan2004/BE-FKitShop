package com.group4.FKitShop.Service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + "Product" + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String uploadFile(MultipartFile multipartFile, String folderName) { // Add folderName parameter
        if(multipartFile.isEmpty()) {
            return "";
        }
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);

            // Include folder name in the S3 object key
            fileUrl = endpointUrl + "/" + bucketName + "/" + folderName + "/" + fileName;
            uploadFileTos3bucket(folderName + "/" + fileName, file); // Upload with folder name
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String folderName) {
        List<String> fileUrls = new ArrayList<>();  // List to store URLs of uploaded files

        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return fileUrls;  // Return empty list if no files were provided
        }

        try {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    File file = convertMultiPartToFile(multipartFile);  // Convert each file
                    String fileName = generateFileName(multipartFile);  // Generate unique file name

                    // Include folder name in the S3 object key
                    String fileUrl = endpointUrl + "/" + bucketName + "/" + folderName + "/" + fileName;
                    uploadFileTos3bucket(folderName + "/" + fileName, file);  // Upload with folder name

                    fileUrls.add(fileUrl);  // Add uploaded file URL to list
                    file.delete();  // Delete temporary file after upload
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileUrls;  // Return the list of uploaded file URLs
    }

    //convert MultipartFile -> File
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    //ignore same name
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    //upload file to bucket
    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }





}