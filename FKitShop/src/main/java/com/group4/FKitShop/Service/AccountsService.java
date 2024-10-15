package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Exception.MultiAppException;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Request.*;
import com.group4.FKitShop.Mapper.AccountsMapper;
import com.group4.FKitShop.Response.AccountsResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountsService {

    @Autowired
    AccountsRepository accountsRepository;
    @Autowired
    AccountsMapper accountsMapper;
    @Autowired
    AmazonClient amazonClient;
    @Value("${amazonProperties.Default.image}")
    String defaultImage;
    @Value("${amazonProperties.folder.Account}")
    private String folderName;
// ==============================a Minh
//    public Accounts createAccount(AccountsRequest request) throws MultiAppException {
//        List<AppException> exceptions = new ArrayList<>();
//
//        if (accountsRepository.existsByemail(request.getEmail())){
//            exceptions.add(new AppException(ErrorCode.PHONE_EXISTED));
//        }
//        if (exceptions.isEmpty()){
//            throw new MultiAppException(exceptions);
//        }
//        accounts.setRole("user");
//        accounts.setStatus(1);
//        // accounts.setCreateDate(new D);
//        accounts.setCreateDate(new Date(System.currentTimeMillis()));
//        //su dung brcrypt de ma hoa password
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        accounts.setPassword(passwordEncoder.encode(request.getPassword()));
//        return accountsRepository.save(accounts);
//    }

//    public Accounts register(AccountCustomerRequest request) throws MultiAppException {
//        List<AppException> exceptions = new ArrayList<>();
//
//        if (accountsRepository.existsByemail(request.getEmail())){
//        }
//        if (accountsRepository.existsByphoneNumber(request.getPhoneNumber())){
//            exceptions.add(new AppException(ErrorCode.PHONE_EXISTED));
//        }
//        if (exceptions.isEmpty()){
//            throw new MultiAppException(exceptions);
//        }
//
//        Accounts accounts = accountsMapper.toAccounts(request);
//        accounts.setRole("user");
//        accounts.setStatus(1);
//        accounts.setCreateDate(new Date(System.currentTimeMillis()));
//        //su dung brcrypt de ma hoa password
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        accounts.setPassword(passwordEncoder.encode(request.getPassword()));
//        return accountsRepository.save(accounts);
//    }
// anh Minh

    public Accounts register(AccountCustomerRequest request) {
        if (accountsRepository.existsByemail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXSITED);
        }
        if (accountsRepository.existsByphoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        String test = String.valueOf(request.getPhoneNumber().charAt(0));
        if (!test.equals("0") && request.getPhoneNumber().length() == 10) {
            throw new AppException(ErrorCode.PHONE_ERROR);
        }

        Accounts accounts = accountsMapper.toAccounts(request);
        accounts.setImage(defaultImage);
        accounts.setRole("user");
        accounts.setStatus(1);
        // accounts.setCreateDate(new D);
        accounts.setCreateDate(new Date(System.currentTimeMillis()));
        //su dung brcrypt de ma hoa password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        accounts.setPassword(passwordEncoder.encode(request.getPassword()));
        return accountsRepository.save(accounts);
    }

    public List<Accounts> allAccounts() {
        return accountsRepository.findAll();
    }

    public List<Accounts> getActiveAccounts() {
        return accountsRepository.getActiveAccounts();
    }

    public Optional<Accounts> getAccountByID(String id) {
        return accountsRepository.findById(id);
    }

    public String updatePassword(UpdatePassword request, String id) {
        Accounts accounts = accountsRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        if (request.getNewPassword().length() < 6 || request.getCurrentPassword().length() < 6) {
            throw new AppException(ErrorCode.USER_PASS_LENGHT);
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        if (!encoder.matches(request.getCurrentPassword(), accounts.getPassword()))
            throw new AppException(ErrorCode.USER_PASS_INCORECT);

        accounts.setPassword(encoder.encode(request.getNewPassword()));

        accountsRepository.save(accounts);
        return "";
    }

    public Accounts updateInfo(String id, UpdateInfoCustomerRequest request) {
        Accounts accounts = accountsRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        if (accountsRepository.existsByemail(request.getEmail()) && !request.getEmail().equals(accounts.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXSITED);
        }

        if (accountsRepository.existsByphoneNumber(request.getPhoneNumber()) && !request.getPhoneNumber().equals(accounts.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }
        accountsMapper.toAccounts(request, accounts);
        return accountsRepository.save(accounts);
    }

    public String deleteAccount(String id) {
        Accounts accounts = accountsRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        accounts.setStatus(0);
        accountsRepository.save(accounts);
        return "";
    }

    public String activeAccount(String id) {
        Accounts accounts = accountsRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        if (accounts.getStatus() == 0) {
            accounts.setStatus(1);
        }
        accountsRepository.save(accounts);
        return "";
    }

    public String uploadAvatar(String id, MultipartFile image) {
        Accounts accounts = accountsRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        String imageUrl = amazonClient.uploadFile(image, folderName);

        if (imageUrl != "") {
            accounts.setImage(imageUrl);
        }
        accountsRepository.save(accounts);
        return "";
    }

    public Accounts createAccount(AccountsRequest request) {
        if (accountsRepository.existsByemail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXSITED);
        }
        if (accountsRepository.existsByphoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        request.setStatus(1);
        Accounts accounts = accountsMapper.toAccounts(request);
        accounts.setCreateDate(new Date(System.currentTimeMillis()));
        accounts.setImage(defaultImage);
        //su dung brcrypt de ma hoa password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        accounts.setPassword(passwordEncoder.encode(request.getPassword()));
        return accountsRepository.save(accounts);
    }

    public Accounts updateAccount(AccountsRequest request, String id) {
        Accounts accounts = accountsRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );

        if (accountsRepository.existsByemail(request.getEmail()) && !request.getEmail().equals(accounts.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXSITED);
        }

        if (accountsRepository.existsByphoneNumber(request.getPhoneNumber()) && !request.getPhoneNumber().equals(accounts.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        accountsMapper.toAccounts(request, accounts);
        return accountsRepository.save(accounts);
    }

    public Accounts updateAccountByAdmin(AccountAdminRequest request, String id) {
        Accounts accounts = accountsRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );

        if (accountsRepository.existsByemail(request.getEmail()) && !request.getEmail().equals(accounts.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXSITED);
        }

        if (accountsRepository.existsByphoneNumber(request.getPhoneNumber()) && !request.getPhoneNumber().equals(accounts.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        accounts.setAdminID(request.getAdminID());
        accounts.setFullName(request.getFullName());
        accounts.setEmail(request.getEmail());
        accounts.setDob(request.getDob());
        accounts.setPhoneNumber(accounts.getPhoneNumber());
        accounts.setImage(accounts.getImage());
        accounts.setRole(request.getRole());
        accounts.setStatus(request.getStatus());

        return accountsRepository.save(accounts);
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


}
