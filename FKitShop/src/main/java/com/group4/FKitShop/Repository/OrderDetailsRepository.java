package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, String> {

    List<OrderDetails> findAllByordersID(String ordersID);

    @Query(value = "select * \n" +
            "from OrderDetails\n" +
            "where ordersID = :ordersID and isActive = :status", nativeQuery = true)
    List<OrderDetails> findActiveOrderDetails(String ordersID, int status);

//    List<OrderDetails> findByOrdersID(String ordersID);

    @Query(value = "select od.* \n" +
            "from OrderDetails od join StemProduct stem on od.productID = stem.productID\n" +
            "where od.ordersID = :ordersID and stem.type = 'kit'", nativeQuery = true)
    List<OrderDetails> findByOrdersID(@Param("ordersID") String ordersID);
}

