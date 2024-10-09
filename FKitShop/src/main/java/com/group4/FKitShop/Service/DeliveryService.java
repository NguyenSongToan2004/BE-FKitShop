package com.group4.FKitShop.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.FKitShop.Request.ServiceFeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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


    public ResponseEntity<JsonNode> getProvinces() {
        String api = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
        // Tạo headers cho request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        // Đóng gói headers thành HttpEntity
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        // Gửi yêu cầu GET đến API GHN
        ResponseEntity<JsonNode> response = restTemplate.exchange(api, HttpMethod.GET, entity, JsonNode.class);
        return response; // Trả về đối tượng JSON đã parse
    }

    public ResponseEntity<JsonNode> getDistrict(int provinceID) {
        try {
            String api = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Token", token);
            System.out.println("toi day");
            String requestBody = "{\"province_id\": " + provinceID + "}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(api, HttpMethod.POST, entity, JsonNode.class);
            return response;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public ResponseEntity<JsonNode> getWard(int districtID) {
        try {
            String api = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Token", token);
            System.out.println("toi day");
            String requestBody = "{\"district_id\": " + districtID + "}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(api, HttpMethod.POST, entity, JsonNode.class);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Value("${spring.shop.from-district-id}")
    private int from_district_id;

    public ResponseEntity<JsonNode> calculateServicePrice(ServiceFeeRequest request) {
        try {
            String api = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Token", token);

            // Tạo body cho request
            String requestBody = "{"
                    + "\"service_id\": 53321,"
                    + "\"insurance_value\": " + request.getService_cost() + ","
                    + "\"from_district_id\": " + from_district_id + ","
                    + "\"to_district_id\": " + request.getTo_district_id() + ","
                    + "\"to_ward_code\": \"" + request.getTo_ward_code() + "\","
                    + "\"height\": " + 5 + ","
                    + "\"length\": " + 10 + ","
                    + "\"weight\": " + 150 + ","
                    + "\"width\": " + 10
                    + "}";
            // Đóng gói headers và body thành HttpEntity
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(api, HttpMethod.POST, entity, JsonNode.class);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
