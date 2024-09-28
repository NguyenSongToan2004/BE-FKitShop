package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Request.AuthenticationRequest;
import com.group4.FKitShop.Response.AuthenticationResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    AccountsRepository accountsRepository;

    //no injection this into constructor
    @NonFinal
    protected static final String SIGNER_KEY =
            "toM/m9xtJPF+QFijlzC6azr3XV7x9JExG8KR0gP7IU3gOf6mTqCAxve2dEWq6bQ7";


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = accountsRepository.findByemail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean auth = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!auth)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String accountID = String.valueOf(user.getAccountID());

        var token = generateToken(request.getEmail(), accountID);

        return AuthenticationResponse.builder()
                .token(token)
                .isAutheticated(true)
                .build();

    }

    private String generateToken(String email, String accountID) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //build payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                //user login
                .subject(email)
                //domain
                .issuer("fkshop.domain")
                .claim("accountID", accountID)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))

                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);
        //sign
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }
    }


}
