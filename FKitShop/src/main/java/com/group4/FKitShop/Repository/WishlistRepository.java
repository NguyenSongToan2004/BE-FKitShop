package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {


    List<Wishlist> findByAccountID(String id);

    List<Wishlist> findByProductID(String id);

    // check wishlist existed
    @Query(value = "select * from Wishlist where accountID = :accountID and productID = :productID", nativeQuery = true)
    Wishlist checkWishlistByAccountIDAndProductID(@Param("accountID") String accountID, @Param("productID") String productID);
}
