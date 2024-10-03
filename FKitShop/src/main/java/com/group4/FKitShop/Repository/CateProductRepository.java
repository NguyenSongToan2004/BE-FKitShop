package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.CateProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CateProductRepository extends JpaRepository<CateProduct, Integer> {

    List<CateProduct> findByCategoryID(String id);
    List<CateProduct> findByProductID(String id);

}
