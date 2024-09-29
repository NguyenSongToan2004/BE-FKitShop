package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Mapper.AccountsMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountsService {

    AccountsRepository accountsRepository;
    AccountsMapper accountsMapper;


    public Accounts createAccount(AccountsRequest request){
        if (accountsRepository.existsByemail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXSITED);
        }
        if (accountsRepository.existsByphoneNumber(request.getPhoneNumber())){
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }
        request.setRole("user");
        request.setStatus(1);
        request.setCreateDate(new Date());

        Accounts accounts = accountsMapper.toAccounts(request);
        //su dung brcrypt de ma hoa password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        accounts.setPassword(passwordEncoder.encode(request.getPassword()));
        return accountsRepository.save(accounts);
    }

    public List<Accounts> allAccounts(){
        return accountsRepository.findAll();
    }

    public Optional<Accounts> AccountsByID(String id){
        return accountsRepository.findById(id);
    }



}
