package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Entity.OrderDetails;
import com.group4.FKitShop.Entity.Supporting;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Repository.OrderDetailsRepository;
import com.group4.FKitShop.Repository.SupportingRepository;
import com.group4.FKitShop.Request.SupportStatusUpdateRequest;
import com.group4.FKitShop.Request.SupportingRequest;
import com.group4.FKitShop.Request.UpdateSupportDate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupportingService {

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    SupportingRepository supportingRepository;

    public Supporting createSupporting(SupportingRequest request) {
        Accounts accounts = accountsRepository.findById(request.getAccountID()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        OrderDetails orderDetails = orderDetailsRepository.findById(request.getOrderDetailsID()).orElseThrow(
                () -> new AppException(ErrorCode.ORDERDETAILS_NOTFOUND)
        );
        if(orderDetails.getSupportCount() == 0 )
            throw new AppException(ErrorCode.SUPPORTING_LIMITED);
        // nếu đã có 1 request về 1 product (chưa done) thì ko được gửi tiếp
        Supporting supporting = new Supporting();
        supporting.setAccount(accounts);
        supporting.setOrderDetail(orderDetails);
        supporting.setDescription(request.getDescription());
        supporting.setStatus(0);
        supporting.setPostDate(new Date(System.currentTimeMillis()));
         // orderDetails.setSupportCount(5);
        return supportingRepository.save(supporting);
    }

    public Supporting updateStatus(SupportStatusUpdateRequest request) {
        Supporting supporting = supportingRepository.findById(request.getSupportingID()).orElseThrow(
                () -> new AppException(ErrorCode.SUPPORTING_NOT_FOUND)
        );
        supporting.setStatus(request.getStatus());
        if(request.getStatus() == 2)
            supporting.getOrderDetail().setSupportCount(supporting.getOrderDetail().getSupportCount()-1);
        return supportingRepository.save(supporting);
    }

    public List<Supporting> getAllSupport(){
        return supportingRepository.findAll();
    }

    public Set<Supporting> getSupportByAccount(String accountID){
        Accounts accounts = accountsRepository.findById(accountID).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        return accounts.getSupportings();
    }

    public List<Supporting> getSupportByStatus(int status){
        if(status != 0 && status != 1 && status != 2)
            throw new AppException(ErrorCode.SUPPORTING_UNSUPPORTED_STATUS_CODE);
        return supportingRepository.findStatusDone(status);
    }

    public List<Supporting> getSupportByAccount(String accountID, int status){
        if(status != 0 && status != 1 && status != 2)
            throw new AppException(ErrorCode.SUPPORTING_UNSUPPORTED_STATUS_CODE);
        if(!accountsRepository.existsById(accountID))
            throw new AppException(ErrorCode.USER_NOT_EXIST);
        return supportingRepository.findSupportByAccount(accountID, status);
    }

    public Supporting updateSupportDate(UpdateSupportDate request){
        Supporting supporting = supportingRepository.findById(request.getSupportingID()).orElseThrow(
                () -> new AppException(ErrorCode.SUPPORTING_NOT_FOUND)
        );
        Date currentDate = new Date(System.currentTimeMillis());
//        System.out.println("current date : " +currentDate);
//        System.out.println("request date " + request.getDate());
//        System.out.println(currentDate.toLocalDate().equals(request.getDate().toLocalDate()));
        if (currentDate.after(request.getDate()) && !currentDate.toLocalDate().equals(request.getDate().toLocalDate()))
            throw new AppException(ErrorCode.SUPPORTING_INVALID_SUPPORT_DATE);
        supporting.setSupportDate(request.getDate());
        return supportingRepository.save(supporting);
    }
}
