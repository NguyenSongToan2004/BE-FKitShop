package com.group4.FKitShop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.FKitShop.Request.AccountCustomerRequest;
import com.group4.FKitShop.ResponseObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @CsvFileSource(resources = "/RegisterTestData.csv", numLinesToSkip = 1)
    public void testCreateAccount(String fullName, String email, String password, String phoneNumber,
                                  String dob, int expectedStatus, String expectedMessage) throws Exception {
        String url = "http://localhost:" + port + "/fkshop/auth/register";
        // Tạo đối tượng AccountCustomerRequest để map dữ liệu
        AccountCustomerRequest request = AccountCustomerRequest.builder()
                .fullName(fullName)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .dob(Date.valueOf(dob))
                .build();

        // Chuyển đổi request thành JSON
        String requestJson = objectMapper.writeValueAsString(request);

        // Thiết lập tiêu đề HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo HttpEntity bao gồm body (requestJson) và headers
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        // Gửi POST request và nhận phản hồi
        ResponseEntity<ResponseObjectRegis> response = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseObjectRegis.class);

        // Kiểm tra kết quả phản hồi
        //assertEquals(HttpStatus.OK, response.getStatusCode()); // Kiểm tra HTTP Status 200 OK
       assertEquals(expectedStatus, response.getBody().getStatus()); // Kiểm tra status là 1000
        assertEquals(expectedMessage, response.getBody().getMessage()); // Kiểm tra message
//        assertEquals("John Doe", ((Map)response.getBody().getData()).get("fullName")); // Kiểm tra data trả về
    }
}