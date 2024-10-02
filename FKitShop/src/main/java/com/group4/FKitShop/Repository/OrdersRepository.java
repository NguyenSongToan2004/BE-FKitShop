package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findAllByaccountID(String strings);
}
