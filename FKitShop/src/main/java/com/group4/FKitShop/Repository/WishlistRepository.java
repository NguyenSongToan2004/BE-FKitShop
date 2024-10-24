package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    // get wishlist by accountid
    @Query(value = "select * from Wishlist\n" +
            "    where accountID = :id", nativeQuery = true)
    List<Wishlist> getWishlistByAccountID(@Param("id") String id);

    // get wishlist by productid
    @Query(value = "select * from Wishlist\n" +
            "    where productID = :id", nativeQuery = true)
    List<Wishlist> getWishlistByProductID(@Param("id") String id);

    // check wishlist existed
    @Query(value = "select * from Wishlist where accountID = :accountID and productID = :productID", nativeQuery = true)
    Wishlist checkWishlistByAccountIDAndProductID(@Param("accountID") String accountID, @Param("productID") String productID);
}
