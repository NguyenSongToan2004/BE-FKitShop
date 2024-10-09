package com.group4.FKitShop.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeliveryService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.shop.Token}")
    private String token;

    @Value("${spring.shop.ShopID}")
    private String shopID;

    public String getProvinceOrderCity(){
        // URL API của GHN
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";

        // Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Token", token); // Thay bằng Token của bạn

        // Tạo HttpEntity chứa headers và body
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Gửi request đến API GHN
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        // Trả về phản hồi của API
        return response.getBody();
    }
}
