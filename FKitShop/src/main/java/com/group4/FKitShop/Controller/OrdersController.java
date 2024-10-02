package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.Orders;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.OrdersRequest;
import com.group4.FKitShop.Service.OrdersService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrdersController {

    OrdersService ordersService;

    @PostMapping("/checkout")
    public ResponseObject checkout(@RequestBody @Valid OrdersRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Checkout successful")
                .data(ordersService.createOrder(request))
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
        return ResponseObject.builder()
                .status(1000)
                .data(ordersService.getOrdersByAccountID(accountID))
                .build();
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



}
