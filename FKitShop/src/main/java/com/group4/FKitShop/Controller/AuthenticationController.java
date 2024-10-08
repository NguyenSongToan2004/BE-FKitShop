package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Exception.MultiAppException;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Request.AuthenticationRequest;
import com.group4.FKitShop.Request.IntrospectRequest;
import com.group4.FKitShop.Response.AuthenticationResponse;
import com.group4.FKitShop.Service.AccountsService;
import com.group4.FKitShop.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    AccountsService accountsService;

    @PostMapping("/register")
    public ResponseObject createAccounts(@RequestBody @Valid AccountsRequest request) throws MultiAppException {
        return ResponseObject.builder()
                .status(1000)
                .message("Create account successfully")
                .data(accountsService.createAccount(request))

                .build();
    }

    //map authenRequest then return to the ResponseObject
    @PostMapping("/login")
    ResponseObject loginAuthentication(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ResponseObject.builder()
                .status(1000)
                .data(result)
                .build();
    }

    @PostMapping("/introspect")
    ResponseObject loginAuthentication(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ResponseObject.builder()
                .data(result)
                .build();
    }

}
