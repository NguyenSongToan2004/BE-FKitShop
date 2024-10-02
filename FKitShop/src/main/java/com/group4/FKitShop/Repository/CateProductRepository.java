package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.CateProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CateProductRepository extends JpaRepository<CateProduct, Integer> {

}
