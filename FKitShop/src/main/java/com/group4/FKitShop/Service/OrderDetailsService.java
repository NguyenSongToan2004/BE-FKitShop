package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.OrderDetails;
import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.OrderDetailsMapper;
import com.group4.FKitShop.Repository.OrderDetailsRepository;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.OrderDetailsRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderDetailsService {
    OrderDetailsRepository orderDetailsRepository;
    ProductRepository productRepository;
    OrderDetailsMapper orderDetailsMapper;

    public String generateUniqueCode() {
        int number = 1;
        String code;
        do {
            code = String.format("OD%05d", number);
            number++;
        } while (orderDetailsRepository.existsById(code));
        return code;
    }

    public OrderDetails createOrderDetails(OrderDetailsRequest request) {
        try {
            Product product = productRepository.findById(request.getProductID())
                    .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOTFOUND));
            OrderDetails orderDetails = orderDetailsMapper.toOrderDetails(request);
            orderDetails.setOrderDetailsID(generateUniqueCode());
            orderDetails.setPrice(product.getPrice()*orderDetails.getQuantity());
            orderDetails.setIsActive(0);
            orderDetails.setStatus("Not-delivery");
            orderDetails.setConfirmDate(new Date());
            return orderDetailsRepository.save(orderDetails);
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
