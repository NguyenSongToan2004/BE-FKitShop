package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Service.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VNPAYController {
    @Autowired
    private VNPAYService vnPayService;
    @Autowired
    private HttpServletResponse response;

    @GetMapping({"", "/"})
    public String home() {
        return "createOrder";
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                            @RequestParam("orderInfo") String orderInfo,
                            HttpServletRequest request) throws IOException {
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, "http://localhost:8080/fkshop/home");
        System.out.println("Vn pay url : " + vnpayUrl);
        return vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/home")
    public void paymentCompleted(HttpServletRequest request) throws IOException {
        int paymentStatus = vnPayService.orderReturn(request);
        if (paymentStatus == 1)
            response.sendRedirect("http://localhost:5173/order-success");
        else
            response.sendRedirect("http://localhost:5173/cart");
    }
}
