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
import com.group4.FKitShop.Service.OrderDetailsService;
import com.group4.FKitShop.Service.OrderStatusService;
import com.group4.FKitShop.Service.OrdersService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final OrderStatusService orderStatusService;

    //bussiness flow
    @PostMapping("/checkout")
    @Transactional
    public ResponseObject checkout(@RequestBody @Valid CheckoutRequest request) {
        try {
            //create order
            //ensure order save and immediately flushes the changes to the database
            Orders orders = ordersService.createOrder(request.getOrdersRequest());
            if (orders == null) {
                throw new AppException(ErrorCode.ORDER_CREATION_FAILED);
            }
            // create order status
            orderStatusService.createOrderStatus(orders.getOrdersID(), orders.getStatus());

            //create order details by orderid
            String ordersID = orders.getOrdersID();
            List<OrderDetails> details = orderDetailsService.createOrderDetails(request.getOrderDetailsRequest(), ordersID);
            //update totalPrice in order
            //including shipping price
            double totalPrice = orders.getShippingPrice();

            for (OrderDetails detail : details) {
                totalPrice += detail.getPrice() * detail.getQuantity();

            }
            //update totalprice
            orders = ordersService.updateTotalPrice(totalPrice, orders.getOrdersID());

            return ResponseObject.builder()
                    .status(1000)
                    .message("Create Order successfully")
                    .data(new CheckoutResponse(orders, details))
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.ORDER_FAILED);
        }


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


}
