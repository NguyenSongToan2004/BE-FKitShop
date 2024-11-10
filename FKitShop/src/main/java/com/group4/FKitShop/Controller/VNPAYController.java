package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.CheckoutRequest;
import com.group4.FKitShop.Service.OrdersService;
import com.group4.FKitShop.Service.VNPAYService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class VNPAYController {
    @Autowired
    private VNPAYService vnPayService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private OrdersService ordersService;

    @GetMapping({"", "/"})
    public String home() {
        return "createOrder";
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int totalAmount,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String vnpayUrl = vnPayService.createOrder(request, totalAmount, orderInfo, "https://fk-shop.vercel.app/handle-vnpay");
        System.out.println("Vn pay url : " + vnpayUrl);
        return vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/check-out")
    public void paymentCompleted(HttpServletRequest request) throws IOException {
        int paymentStatus = vnPayService.orderReturn(request);
        if (paymentStatus == 1) {
            CheckoutRequest checkoutRequest = (CheckoutRequest) request.getAttribute("order");
            if(checkoutRequest != null) {
                ordersService.checkOut(checkoutRequest);
            } else {
                System.out.println("order is null");
            }
            response.sendRedirect("http://localhost:5173/order-success");
        }
        else {
            response.sendRedirect("http://localhost:5173/cart");
        }
    }

//    @PostMapping("/submitOrder")
//    public String submitOrder(@RequestBody CheckoutRequest checkoutRequest,
//                              HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        String vnpayUrl = vnPayService.createOrder(request, 500000, "xin chao", "http://localhost:5173/order-success");
//        // Sử dụng session để lưu thông tin order
//        HttpSession session = request.getSession();
//        session.setAttribute("order", checkoutRequest);
//        System.out.println(checkoutRequest);
//        System.out.println("Vn pay url : " + vnpayUrl);
//        return vnpayUrl;  // Chuyển hướng tới VNPay
//    }
//
//    @GetMapping("/check-out")
//    public void paymentCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        int paymentStatus = vnPayService.orderReturn(request);
//
//        HttpSession session = request.getSession();
//        CheckoutRequest checkoutRequest = (CheckoutRequest) session.getAttribute("order");  // Lấy thông tin order từ session
//
//        if (paymentStatus == 1) {
//            System.out.println("Thanh toán thành công !!");
//            if (checkoutRequest != null) {
//                ordersService.checkOut(checkoutRequest);
//                session.removeAttribute("order");  // Xóa order khỏi session sau khi xử lý
//            } else {
//                System.out.println("order is null");
//            }
//            response.sendRedirect("http://localhost:5173/order-success");
//        } else {
//            System.out.println("Thanh toán thất bại !!");
//            response.sendRedirect("http://localhost:5173/cart");
//        }
//    }
}
