package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Supporting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingRepository extends JpaRepository<Supporting, Integer> {
}
