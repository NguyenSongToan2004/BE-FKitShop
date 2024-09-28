package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Request.AuthenticationRequest;
import com.group4.FKitShop.Response.AuthenticationResponse;
import com.group4.FKitShop.Service.AuthenticationService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;


    //map authenRequest then return to the ResponseObject
    @PostMapping("/login")
    ResponseObject loginAuthentication(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ResponseObject.builder()
                .data(result)
                .build();

    }

}
