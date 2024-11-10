package com.group4.FKitShop.Service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

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

    public void downloadFileFromS3(String s3Path, File localFile) throws IOException {
        try {
            // Phân tích đường dẫn S3 để lấy thông tin tên bucket và key (đường dẫn)
            String[] pathParts = s3Path.split("/", 2);
            String bucket = pathParts[0];
            String key = pathParts[1];

            // Tạo yêu cầu để lấy file từ S3
            GetObjectRequest request = new GetObjectRequest(bucket, key);
            S3Object s3Object = s3client.getObject(request);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            // Lưu nội dung từ input stream vào file cục bộ
            try (FileOutputStream fileOutputStream = new FileOutputStream(localFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                System.out.println("Downloaded file from S3: " + localFile.getAbsolutePath());
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error downloading file from S3", e);
        }
    }

    private static final String STORAGE_DIRECTORY = "uploads" + File.separator + "lab";

    public File downloadFileFromS3(String fileName) {
        // Tạo đường dẫn đến S3 bucket
        String s3Path = endpointUrl + "/" + bucketName + "/" + "lab" + "/" + fileName;
        File localFile = new File(System.getProperty("user.dir") + File.separator + STORAGE_DIRECTORY + File.separator + fileName);
        if(localFile.exists()) {
            return localFile;
        }
//        // Tải file từ S3 xuống
//        try {
//            downloadFileFromS3(s3Path, localFile);
//            System.out.println("Downloaded file from S3: " + localFile.getAbsolutePath());
//            return localFile;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new AppException(ErrorCode.LAB_DOWNLOAD_FAILED);
//        }
        return null;
    }

//    public String uploadFile(File fileOrigin, String folderName) { // Add folderName parameter
//        if(fileOrigin == null) {
//            return "";
//        }
//        String fileUrl = "";
//        try {
//            // File file = convertMultiPartToFile(fileOrigin);
//            // String fileName = generateFileName(multipartFile);
//
//            // Include folder name in the S3 object key
//            fileUrl = endpointUrl + "/" + bucketName + "/" + folderName + "/" + fileOrigin.getName();
//            uploadFileTos3bucket(folderName + "/" + fileOrigin.getName(), fileOrigin); // Upload with folder name
//            fileOrigin.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return fileUrl;
//    }

    public String uploadFile(File fileOrigin, String folderName) {
        if (fileOrigin == null) {
            return "";
        }
        String fileUrl = "";
        try {
            // Xây dựng URL cho tệp sẽ tải lên S3
            fileUrl = endpointUrl + "/" + bucketName + "/" + folderName + "/" + fileOrigin.getName();

            // Upload file lên S3
            uploadFileTos3bucket(folderName + "/" + fileOrigin.getName(), fileOrigin);

            // Xóa file gốc sau khi đã upload
            if (fileOrigin.exists()) {
                fileOrigin.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
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