package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query(value = "SELECT cartID, accountID, productID, quantity, status FROM Cart\n" +
            "where accountID = :accountID and productID = :ProductID", nativeQuery = true)
    Optional<Cart> findByaccountIDAndproductID(@Param("accountID") String accountID, @Param("ProductID") String ProductID);


    List<Cart> findByaccountID(String accountID);

    @Modifying
    @Transactional
    @Query(value = "delete from Cart\n" +
            "where accountID = :accountID\n" +
            "and productID = :productID",nativeQuery = true)
    void deletebyAccountIDAndProductID(@Param("accountID") String accountID, @Param("productID") String productID);

    Optional<Cart> findByproductID(String productID);

    @Modifying
    @Query(value = "delete from Cart\n" +
            "where accountID = :accountID", nativeQuery = true)
    void clearCartByAccountID(@Param("accountID") String accountID);

}
