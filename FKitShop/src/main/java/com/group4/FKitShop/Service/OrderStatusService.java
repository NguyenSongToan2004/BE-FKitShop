package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.OrderStatus;
import com.group4.FKitShop.Entity.Question;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.OrderStatusRepository;
import com.group4.FKitShop.Request.OrderStatusRequest;
import com.group4.FKitShop.Request.QuestionRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderStatusService {

    OrderStatusRepository orderStatusRepository;

    public List<OrderStatus> allStatus(){
        return orderStatusRepository.findAll();
    }

    public OrderStatus getOrderStatusByID(int id){
        OrderStatus os = orderStatusRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OrderStatus_NOTFOUND));
        return os;
    }

    public OrderStatus createOrderStatus(OrderStatusRequest request) {
        if(orderStatusRepository.checkOSExist(request.getOrdersID(), request.getStatus()) != null){
            throw new AppException(ErrorCode.OrderStatus_EXIST);
        }
        OrderStatus os = new OrderStatus();
        os.setOrdersID(request.getOrdersID());
        os.setStatus(request.getStatus());
        os.setOrderStatusDate(new Date());
        return orderStatusRepository.save(os);
    }

    public OrderStatus updateOrderStatus(int id, OrderStatusRequest request){
        OrderStatus os = orderStatusRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OrderStatus_NOTFOUND));
        os.setStatus(request.getStatus());
        os.setOrdersID(request.getOrdersID());
        os.setOrderStatusDate(new Date());
        return orderStatusRepository.save(os);
    }

    @Transactional
    public void deleteOS(int id) {
        if (!orderStatusRepository.existsById(id))
            throw new AppException(ErrorCode.OrderStatus_NOTFOUND);
        orderStatusRepository.deleteById(id);
    }

    public List<OrderStatus> getOSByOrderID(String id){
        return orderStatusRepository.getOrderStatusDetails(id);
    }
}