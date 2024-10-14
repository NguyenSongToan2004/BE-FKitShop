package com.group4.FKitShop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.FKitShop.ResponseObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void loginAuthenticationTest() throws Exception {
        String urlTest = "http://localhost:" + port + "/fkshop/auth/login";

        // Tạo đối tượng yêu cầu
        String requestJson = "{\"email\":\"leesintoan2@gmail.com\",\"password\":\"12345623123\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        // Gửi yêu cầu POST
        ResponseEntity<String> response = restTemplate.postForEntity(new URL(urlTest).toString(), requestEntity, String.class);

        // Sử dụng ObjectMapper để chuyển đổi JSON thành đối tượng ResponseObject
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseObject responseObject = objectMapper.readValue(response.getBody(), ResponseObject.class);

        // Lấy status và data từ phản hồi
        int status = responseObject.getStatus();
        Object data = responseObject.getData(); // Hoặc kiểu dữ liệu cụ thể nếu cần

        // In ra để kiểm tra
        System.out.println("Status: " + status);
        System.out.println("Data: " + data);

        // Kiểm tra giá trị status
        assertEquals(1006, status);
        // Nếu bạn cần kiểm tra data, bạn cần biết dữ liệu dự kiến
        // assertEquals(expectedData, data); // thêm kiểm tra cho data nếu cần
    }
}
