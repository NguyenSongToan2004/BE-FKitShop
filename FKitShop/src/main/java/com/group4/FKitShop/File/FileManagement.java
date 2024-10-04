package com.group4.FKitShop.File;

import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileManagement {

    private static String UPLOAD_DIRECTORY = "FKitShop"+ File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"uploads";

    public static String uploadImage(MultipartFile file, String directoryUpload) {
        // Kiểm tra xem file có rỗng không
        if (file.isEmpty()) {
            return "";
        }
        try {
            // Lấy đường dẫn tương đối đến thư mục uploads (có thể thay đổi tùy môi trường)
            String uploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIRECTORY + File.separator+ directoryUpload;
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            // Lưu file vào thư mục
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return  "uploads" + File.separator + directoryUpload + File.separator + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }
}
