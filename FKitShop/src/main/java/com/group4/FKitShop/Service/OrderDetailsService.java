package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.OrderDetails;
import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.OrderDetailsRepository;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.OrderDetailsRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderDetailsService {
    OrderDetailsRepository orderDetailsRepository;
    ProductRepository productRepository;


    public String generateUniqueCode() {
        int number = 1;
        String code;
        do {
            code = String.format("OD%05d", number);
            number++;
        } while (orderDetailsRepository.existsById(code));
        return code;
    }

    public List<OrderDetails> createOrderDetails(OrderDetailsRequest request, String ordersID) {
        try {
            //get the product quantities
            Map<String, Integer> productQuantity = request.getProductQuantity();
            List<OrderDetails> orderDetails = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : productQuantity.entrySet()) {
                Product product = productRepository.findById(entry.getKey()).get();
                OrderDetails o = new OrderDetails();
                o.setOrderDetailsID(generateUniqueCode());
                o.setOrdersID(ordersID);
                o.setProductID(product.getProductID());
                o.setQuantity(entry.getValue());
                o.setPrice(product.getPrice() * entry.getValue());
                o.setIsActive(0);
                o.setStatus("inactive");
                o.setConfirmDate(new Date());
                orderDetailsRepository.save(o);
                orderDetails.add(o);
            }
        return orderDetails;
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    public List<OrderDetails> getOrderDetails(){
        return orderDetailsRepository.findAll();
    }

    public List<OrderDetails> getOrderDetailsByOrderID(String orderID) {
        return orderDetailsRepository.findAllByordersID(orderID);
    }

//    public OrderDetails updateOrderDetails(String ordersID, OrderDetailsRequest request) {
//        OrderDetails orderDetails = orderDetailsRepository.findById(ordersID)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDERDETAILS_NOTFOUND));
//
//        orderDetails.set
//
//    }





}
