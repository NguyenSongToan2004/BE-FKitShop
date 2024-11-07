package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.OrderDetails;
import com.group4.FKitShop.Entity.Orders;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Request.*;
import com.group4.FKitShop.Response.CheckoutResponse;
import com.group4.FKitShop.Service.OrderDetailsService;
import com.group4.FKitShop.Service.OrderStatusService;
import com.group4.FKitShop.Service.OrdersService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrdersController {

    OrdersService ordersService;
    OrderDetailsService orderDetailsService;
    private final OrderStatusService orderStatusService;

    @PostMapping("/checkout")
    @Transactional
    public ResponseObject checkout(@RequestBody @Valid CheckoutRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create Order successfully")
                .data(ordersService.checkOut(request))
                .build();
    }

    @GetMapping("/allorders")
    public ResponseObject allOrders() {
        return ResponseObject.builder()
                .status(1000)
                .message("All orders")
                .data(ordersService.getOrders())
                .build();
    }

    @GetMapping("/find/{accountID}")
    public ResponseObject getOrdersByAccountID(@PathVariable String accountID) {
        try {
            List<Orders> ordersList = ordersService.findByAccountID(accountID);
            List<CheckoutResponse> checkoutResponses = new ArrayList<>();
            for (Orders orders : ordersList) {
                CheckoutResponse checkout = new CheckoutResponse();
                checkout.setOrders(orders);
                List<OrderDetails> orderDetails = orderDetailsService.findByOrderID(orders.getOrdersID());
                checkout.setOrderDetails(orderDetails);
                checkoutResponses.add(checkout);
            }
            return ResponseObject.builder()
                    .status(1000)
                    .data(checkoutResponses)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            // e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    @PutMapping("/update/{ordersID}")
    public ResponseObject updateOrders(@PathVariable String ordersID, @RequestBody @Valid OrdersRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update Order successfully")
                .data(ordersService.updateOrder(ordersID, request))
                .build();
    }

    @PutMapping("/updatestatus/{ordersID}")
    public ResponseObject updateStatus(@PathVariable String ordersID, @RequestParam String status) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update Order status successfully")
                .data(ordersService.updateOrderStatus(ordersID, status))
                .build();
    }

    @GetMapping("/details/{ordersID}")
    public ResponseObject getOrderDetailsByOrdersID(@PathVariable String ordersID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Order Details List")
                .data(orderDetailsService.findByOrderID(ordersID))
                .build();
    }

    @GetMapping("/status/{status}")
    public ResponseObject getOrdersByStatus(@PathVariable String status) {
        return ResponseObject.builder()
                .status(1000)
                .message("Order Status List")
                .data(ordersService.findOrderbyStatus(status))
                .build();
    }

    @GetMapping("/report/{time}")
    public ResponseEntity<byte[]> getReport(OutputStream outputStream, @PathVariable("time") String time)
            throws IOException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(ordersService.getOrderReportFile(time));
    }

    @GetMapping("/months")
    public ResponseEntity<ResponseObject> getMonth() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get Months Successfully !!", ordersService.getMonth()));
    }

    @GetMapping("/revenue")
    public ResponseEntity<ResponseObject> getRevenue(@RequestBody @Valid RevenueRequest request) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get Revenue Successfully !!", ordersService.getRevenue(request)));
    }

    @GetMapping("/daily-revenue")
    public ResponseEntity<ResponseObject> getDailyRevenueForCurrentMonth() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get daily revenue successfully !!",
                        ordersService.getDailyRevenueForCurrentMonth())
        );
    }

    @GetMapping("/dailyrevenue")
    public ResponseEntity<ResponseObject> getDailyRevenue(@RequestBody @Valid RevenueRequest request) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get Revenue Successfully !!", ordersService.getDailyRevenue(request)));
    }
}
