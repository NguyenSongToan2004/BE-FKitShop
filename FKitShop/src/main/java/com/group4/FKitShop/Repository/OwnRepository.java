package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Own;
import com.group4.FKitShop.Entity.OwnID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnRepository extends JpaRepository<Own, OwnID> {
    boolean existsByLab_LabIDAndAccount_AccountID(String labID, String accountID);
}
