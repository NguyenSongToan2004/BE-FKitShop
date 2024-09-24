package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Request.AccountsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountsService {
    @Autowired
    private AccountsRepository accountsRepository;


    public List<Accounts> allAccounts(){
        return accountsRepository.findAll();
    }

    public Accounts createAccount(AccountsRequest request){
        Accounts accounts = new Accounts();

        if (accountsRepository.existsByemail(request.getEmail())){
            throw new RuntimeException("This email is already in use");
        }
        accounts.setPassword(request.getPassword());
        accounts.setImage(request.getImage());
        accounts.setFullName(accounts.getFullName());
        accounts.setYob(request.getYob());
        accounts.setPhoneNumber(request.getPhoneNumber());
        accounts.setEmail(request.getEmail());
        accounts.setStatus(request.getStatus());
        accounts.setRole(accounts.getRole());
        accounts.setCreateDate(request.getCreateDate());

        return accountsRepository.save(accounts);
    }


}
