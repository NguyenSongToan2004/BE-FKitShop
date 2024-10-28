package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Entity.Lab;
import com.group4.FKitShop.Entity.Own;
import com.group4.FKitShop.Entity.OwnID;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Repository.LabRepository;
import com.group4.FKitShop.Repository.OwnRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnService {

    LabRepository labRepository;
    OwnRepository ownRepository;
    AccountsRepository accountsRepository;

    public boolean isExist(String accountID, String labID) {
        System.out.println(ownRepository.existsByLab_LabIDAndAccount_AccountID(labID, accountID));
        return ownRepository.existsByLab_LabIDAndAccount_AccountID(labID, accountID);
    }

    public Own getOwn(String accountID, String labID) {
        OwnID ownID = new OwnID(labID, accountID);
        Own own = ownRepository.findById(ownID).orElseThrow(
                () -> new AppException(ErrorCode.OWN_NOT_EXIST)
        );
        return own;
    }

    public void updateSupportTimes(String accountID, String labID, int timesAdd) {
        OwnID ownID = new OwnID(labID, accountID);
        Own own = ownRepository.findById(ownID).orElseThrow(
                () -> new AppException(ErrorCode.OWN_NOT_EXIST)
        );
        own.setSupportTimes(own.getSupportTimes() + timesAdd);
        ownRepository.save(own);
    }

    public void createOwn(String accountID, String labID, int supTimes) {
        if (isExist(accountID, labID))
            throw new AppException(ErrorCode.OWN_IS_EXIST);
        Lab lab = labRepository.findById(labID).orElseThrow(
                () -> new AppException(ErrorCode.LAB_NOTFOUND)
        );
        Accounts account = accountsRepository.findById(accountID).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        OwnID ownID = new OwnID(accountID, labID);
        Own own = Own.builder()
                .ownID(ownID)
                .lab(lab)
                .account(account)
                .supportTimes(supTimes)
                .build();
        ownRepository.save(own);
    }
}
