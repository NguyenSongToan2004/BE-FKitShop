package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(value = "select productID\n" +
            "from StemProduct\n" +
            "order by productID desc\n" +
            "limit 1", nativeQuery = true)
    String getNumberProduct();
    boolean existsByName(String name);

    @Modifying
    @Query(value = "update StemProduct\n" +
            "set status = 'inactive'\n" +
            "where productID = :id", nativeQuery = true)
    int deleteStatus(@Param("id") String id);


}
