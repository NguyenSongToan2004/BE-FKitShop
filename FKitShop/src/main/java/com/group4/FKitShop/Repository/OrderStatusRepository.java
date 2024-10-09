package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {

    @Query(value = "select * from OrderStatus\n" +
            "where ordersID = :id", nativeQuery = true)
    List<OrderStatus> getOrderStatusDetails(@Param("id") String id);

    @Query(value = "select * from OrderStatus\n" +
            " where ordersID = :id and status = :status", nativeQuery = true)
    OrderStatus checkOSExist(@Param("id") String id, @Param("status") String status);

}