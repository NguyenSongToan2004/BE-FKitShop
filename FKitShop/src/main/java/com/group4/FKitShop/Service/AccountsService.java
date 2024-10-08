package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Exception.MultiAppException;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Mapper.AccountsMapper;
import com.group4.FKitShop.Response.AccountsResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountsService {

    AccountsRepository accountsRepository;
    AccountsMapper accountsMapper;

    public Accounts createAccount(AccountsRequest request) throws MultiAppException {
        List<AppException> exceptions = new ArrayList<>();

        if (accountsRepository.existsByemail(request.getEmail())){
        }
        if (accountsRepository.existsByphoneNumber(request.getPhoneNumber())){
            exceptions.add(new AppException(ErrorCode.PHONE_EXISTED));
        }
        if (exceptions.isEmpty()){
            throw new MultiAppException(exceptions);
        }
        request.setRole("user");
        request.setStatus(0);
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
