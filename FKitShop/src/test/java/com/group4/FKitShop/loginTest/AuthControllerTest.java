package com.group4.FKitShop.loginTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.FKitShop.Controller.AuthenticationController;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.AuthenticationRequest;
import com.group4.FKitShop.Service.AccountsService;
import com.group4.FKitShop.Service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AuthenticationController.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Simulates sending HTTP requests to the controller

    @Autowired
    private ObjectMapper objectMapper;  // Converts Java objects to JSON and vice versa

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AccountsService accountsService;

    @Test
    public void testLoginSuccess() throws Exception {
        // Create a valid LoginRequest object with correct credentials
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setEmail("asd98@gmail.com");
        loginRequest.setPassword("123456");

        // Set up mock behavior
        // Assuming successful login returns some kind of token or success message
//        String expectedResponse = "Login successfully";
//        var result = authenticationService.authenticate(loginRequest);// Adjust as necessary
//        ResponseObject responseObject = ResponseObject.builder()
//                .status(1000)
//                .message("Login successfully")
//                .data(result)
//                .build();;
//        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(result);

        // Convert the loginRequest object to JSON format using ObjectMapper
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        // Perform a POST request to /fkshop/auth/login with the requestBody (JSON)

        mockMvc.perform(post("http://localhost:8080/fkshop/auth/login")
                        .with(csrf())
                        .with(httpBasic("asd98@gmail.com", "123456"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        //        .andExpect(content().string(expectedResponse));  // Expect the response body to contain the string "Login successful"
    }

    @Test
    public void testLoginFailure() throws Exception {
        // Create an invalid LoginRequest object with wrong credentials
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setEmail("asd98@gmail.com");
        loginRequest.setPassword("wrongpassword");

        // Set up mock behavior for failure case
//        String expectedErrorMessage = "Invalid credentials"; // Adjust based on your service response
//        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenThrow(new RuntimeException(expectedErrorMessage));

        // Convert the loginRequest object to JSON format using ObjectMapper
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        // Perform a POST request to /fkshop/auth/login with the requestBody (JSON)
        mockMvc.perform(post("/fkshop/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())  // Expect an HTTP 401 (Unauthorized) status
               .andExpect(content().string("User not found"));  // Expect the response body to contain the error message
    }
}
