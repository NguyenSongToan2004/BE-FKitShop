package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.mapper.AccountsMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountsService {

    AccountsRepository accountsRepository;
    AccountsMapper accountsMapper;

    public Accounts createAccount(AccountsRequest request){
        if (accountsRepository.existsByemail(request.getEmail())){
            throw new RuntimeException("This email is already in use");
        }
        Accounts accounts = accountsMapper.toAccounts(request);
        //su dung brcrypt de ma hoa password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        accounts.setPassword(passwordEncoder.encode(request.getPassword()));
        return accountsRepository.save(accounts);
    }

    public List<Accounts> allAccounts(){
        return accountsRepository.findAll();
    }




}
