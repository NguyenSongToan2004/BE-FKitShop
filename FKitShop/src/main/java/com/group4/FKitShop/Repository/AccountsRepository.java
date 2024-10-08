package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String> {
    boolean existsByemail(String s);
    boolean existsByphoneNumber(String s);

    boolean existsByrole(String role);

    Optional<Accounts> findByemail(String email);
}
