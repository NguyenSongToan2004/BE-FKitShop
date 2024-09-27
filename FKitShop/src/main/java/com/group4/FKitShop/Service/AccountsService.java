package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.AccountsMapper;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Request.AddCustomerRequest;
import com.group4.FKitShop.Request.UpdateInfoCustomerRequest;
import com.group4.FKitShop.Request.UpdatePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AccountsService {

    @Autowired
    private AccountsRepository repository;

    public Accounts addCustomer(AddCustomerRequest request) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        Accounts accounts= Accounts.builder()
                .accountID(generateID())
                .role("customer")
                .status(1)
                .createDate(new Date())
                .build();
        System.out.println("sdt : " + request.getPhoneNumber());
        request.setPassword(encoder.encode(request.getPassword()));
        AccountsMapper.INSTANCE.toAccounts(request, accounts);
        return repository.save(accounts);
    }

    public Accounts getCustomerByID(String id) {
        return repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public Accounts updateInfo(String id, UpdateInfoCustomerRequest request) {
        Accounts accounts = repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        System.out.println(request.getDob());
        AccountsMapper.INSTANCE.toAccounts(request, accounts);
        return repository.save(accounts);
    }

    public String uploadAvatar(String id, MultipartFile image) {
        Accounts accounts = repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        String imageUrl = uploadImage(image);

        if (imageUrl != "") {
            accounts.setImage(imageUrl);
        }
        repository.save(accounts);
        return "";
    }

    public String deleteAccount(String id) {
        Accounts accounts = repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        accounts.setStatus(0);
        repository.save(accounts);
        return "";
    }

    public List<Accounts> getActiveAccounts() {
        return repository.getActiveAccounts();
    }

    public String updatePassword(UpdatePassword request, String id) {

        Accounts accounts = repository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        accounts.setPassword(encoder.encode(request.getPassword()));

        repository.save(accounts);
        return "";
    }

    public List<Accounts> getAllAccounts() {
        return repository.findAll();
    }

    private static final String UPLOAD_DIRECTORY = "uploads" + File.separator + "accounts";

    String uploadImage(MultipartFile file) {
        // Kiểm tra xem file có rỗng không
        if (file.isEmpty()) {
            return "";
        }
        try {
            // Lấy đường dẫn tương đối đến thư mục uploads (có thể thay đổi tùy môi trường)
            String uploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIRECTORY;
            // System.getProperty("user.dir") : lấy ra đường dẫn đến thư mục hiện tại
            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            // Lưu file vào thư mục
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return UPLOAD_DIRECTORY + File.separator + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    String generateID() {
        String num = repository.getNumberAccounts();
        int max = Integer.parseInt(num.substring(1, 6)) + 1;
        num = String.format("A%05d", max);
        return num;
    }
}
