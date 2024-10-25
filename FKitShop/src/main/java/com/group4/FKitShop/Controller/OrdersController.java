package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.OrderDetails;
import com.group4.FKitShop.Entity.Orders;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Request.CheckoutRequest;
import com.group4.FKitShop.Request.OrderDetailsRequest;
import com.group4.FKitShop.Request.OrdersRequest;
import com.group4.FKitShop.Response.CheckoutResponse;
import com.group4.FKitShop.Service.AuthenticationService;
import com.group4.FKitShop.Service.OrderDetailsService;
import com.group4.FKitShop.Service.OrderStatusService;
import com.group4.FKitShop.Service.OrdersService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    AuthenticationService authenticationService;
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
    @PreAuthorize("hasRole('admin') or hasRole('manager')")
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
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    @GetMapping("/findOrder")
    public ResponseObject findOrder(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        String accountID = (authenticationService.tokenAccountResponse(token)).getAccountID();
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
            //e.getMostSpecificCause().getMessage()
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

    @GetMapping("details/{ordersID}")
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
}
