package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Entity.LogoutToken;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Repository.LogoutTokenRepository;
import com.group4.FKitShop.Request.AuthenticationRequest;
import com.group4.FKitShop.Request.IntrospectRequest;
import com.group4.FKitShop.Request.LoginGoogleRequest;
import com.group4.FKitShop.Request.LogoutRequest;
import com.group4.FKitShop.Response.AccountsResponse;
import com.group4.FKitShop.Response.AuthenticationResponse;
import com.group4.FKitShop.Response.IntrospectResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    AccountsRepository accountsRepository;
    LogoutTokenRepository logoutTokenRepository;

    //no injection this into constructor
    @NonFinal
    //read yaml file
//    @Value("${jwt.signerKey}")
    protected static final String SIGNER_KEY = "toM/m9xtJPF+QFijlzC6azr3XV7x9JExG8KR0gP7IU3gOf6mTqCAxve2dEWq6bQ7";


    public AuthenticationResponse authenticateGG(LoginGoogleRequest request) {
        var user = accountsRepository.findByemail(request.getEmail())
                .orElseGet(() -> Accounts.builder()
                        .email(request.getEmail())
                        .image(request.getPicture())
                        .fullName(request.getName())
                        .createDate(new java.sql.Date(System.currentTimeMillis()))
                        .status(1)
                        .role("user")
                        .build());
        if (!accountsRepository.existsByemail(request.getEmail()))
            accountsRepository.save(user);
        // sai mật khẩu
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .accounts(user)
                .token(token)
                .isAutheticated(true)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = accountsRepository.findByemail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean auth = passwordEncoder.matches(request.getPassword(), user.getPassword());
        // sai mật khẩu
        if (!auth)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .accounts(user)
                .token(token)
                .isAutheticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        boolean isValid = true;
        var token = request.getToken();
        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AccountsResponse tokenAccountResponse(String token) {
        try {
            // Parse the JWS token
            JWSObject jwsObject = JWSObject.parse(token);

            // Verify the signature using the secret key
            if (jwsObject.verify(new MACVerifier(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)))) {
                // log.info("tokenResponse: Token signature is valid");

                // Extract the payload
                JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());

                // Extract token details (claims)
                String accountID = claimsSet.getStringClaim("accountID");
                String fullName = claimsSet.getStringClaim("fullName");
                String image = claimsSet.getStringClaim("image");
                String phoneNumber = claimsSet.getStringClaim("phoneNumber");
                String email = claimsSet.getStringClaim("email");
                Date dob = claimsSet.getDateClaim("dob");
                int status = claimsSet.getIntegerClaim("status");
                String role = claimsSet.getStringClaim("role");
                Date createDate = claimsSet.getDateClaim("createDate");
                String adminID = claimsSet.getStringClaim("adminID");

                return AccountsResponse.builder()
                        .accountID(accountID)
                        .fullName(fullName)
                        .image(image)
                        .phoneNumber(phoneNumber)
                        .status(status)
                        .email(email)
                        .dob(dob)
                        .role(role)
                        .createDate(createDate)
                        .adminID(adminID)
                        .build();
            } else {
                log.error("Invalid token signature");

                throw new AppException(ErrorCode.INVALID_TOKEN);
            }

        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var token = verifyToken(request.getToken());
        String jwtID = token.getJWTClaimsSet().getJWTID();
        String accountID = token.getJWTClaimsSet().getStringClaim("accountID");
        Date expiredDate = token.getJWTClaimsSet().getExpirationTime();
        LogoutToken logoutToken = LogoutToken.builder()
                .accountID(accountID)
                .jwtID(jwtID)
                .expiredDate(expiredDate)
                .build();
        logoutTokenRepository.save(logoutToken);
    }

    //============================================================================================

    private String generateToken(Accounts a) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //build payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                //user login
                .subject(a.getEmail())
                //domain
                .issuer("FKShop")
                .jwtID(UUID.randomUUID().toString())
                .claim("accountID", a.getAccountID())
                .claim("image", a.getImage())
                .claim("fullName", a.getFullName())
                .claim("phoneNumber", a.getPhoneNumber())
                .claim("email", a.getEmail())
                .claim("dob",a.getDob())
                .claim("status", a.getStatus())
                //account role: scope
                .claim("role", a.getRole())
                .claim("createDate", a.getCreateDate())
                .claim("adminID", a.getAdminID())
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

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        //get token date
        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        String jwtID = signedJWT.getJWTClaimsSet().getJWTID();

        //token doesnt change => true, else false
        var verified = signedJWT.verify(verifier);

        //check if token is from the app or expired
        if (!verified && expiredTime.after(new Date()))
            throw new AppException(ErrorCode.INVALID_TOKEN);
        //check if token is logged out
        var isLogout = logoutTokenRepository.findByjwtID(jwtID);
        if (isLogout.isPresent())
            throw new AppException(ErrorCode.INVALID_TOKEN);

        return signedJWT;
    }


}
