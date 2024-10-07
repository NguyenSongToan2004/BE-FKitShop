package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, String> {

    List<OrderDetails> findAllByordersID(String ordersID);
}