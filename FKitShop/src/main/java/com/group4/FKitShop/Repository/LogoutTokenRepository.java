package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogoutTokenRepository extends JpaRepository<LogoutToken, Integer> {
    Optional<LogoutToken> findByjwtID(String jwtID);
}
