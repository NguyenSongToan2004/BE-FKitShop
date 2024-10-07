package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Request.TokenRequest;
import com.group4.FKitShop.Response.AccountsResponse;
import com.group4.FKitShop.Service.AccountsService;
import com.group4.FKitShop.Service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountsController {
    AccountsService accountsService;
    AuthenticationService authenticationService;

    @GetMapping
    public ResponseObject allAccounts(){
        //manager & admin moi dc truy cap trang nay
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //lay thong tin nguoi dang nhap
        log.info("AccountID: {}", authentication.getName());
        //role
        authentication.getAuthorities().forEach(grantedAuthority
                -> log.info(grantedAuthority.getAuthority()));


        return ResponseObject.builder()
                .data(accountsService.allAccounts())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject AccountByID(@PathVariable String id){
        Optional<Accounts> accounts = accountsService.AccountsByID(id);
        return ResponseObject.builder()
                .data(accounts)
                .build();
    }


    @GetMapping("/info")
    public ResponseObject AccountsInfo(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Lấy token từ header
        AccountsResponse accountsResponse = authenticationService.tokenAccountResponse(token);

        return ResponseObject.builder()
                .data(accountsResponse)
                .build();
    }

}
