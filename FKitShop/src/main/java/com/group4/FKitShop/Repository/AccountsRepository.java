package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String> {
    boolean existsByemail(String s);

    boolean existsByphoneNumber(String s);

    boolean existsByrole(String role);

    Optional<Accounts> findByemail(String email);

//    @Query(value = "select accountID\n" +
//            "from Accounts\n" +
//            "order by accountID desc\n" +
//            "limit 1", nativeQuery = true)
//    String getNumberAccounts();

    // lấy ra các account đang active
    @Query(value = "select *\n" +
            "from Accounts\n" +
            "where status = 1", nativeQuery = true)
    List<Accounts> getActiveAccounts();

    Optional<Accounts> findByrole(String role);


//    @Query(value = "SELECT accountID,\n" +
//            "       COUNT(ordersID) AS totalOrders\n" +
//            "FROM Orders\n" +
//            "GROUP BY accountID\n" +
//            "ORDER BY COUNT(ordersID) DESC", nativeQuery = true)
//    HashMap<String, Integer> getCustomerWithOrders();

    @Query(value = "SELECT accountID, COUNT(ordersID) AS totalOrders " +
            "FROM Orders " +
            "GROUP BY accountID " +
            "ORDER BY COUNT(ordersID) DESC", nativeQuery = true)
    List<Object> getCustomerWithOrders();

}
