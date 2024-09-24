package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String> {
    boolean existsByemail(String s);
}