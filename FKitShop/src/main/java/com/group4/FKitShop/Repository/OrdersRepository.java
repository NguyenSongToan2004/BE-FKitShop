package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.Cart;
import com.group4.FKitShop.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findAllByaccountID(String strings);



}
