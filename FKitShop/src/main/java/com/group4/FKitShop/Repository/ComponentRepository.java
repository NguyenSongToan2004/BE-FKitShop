package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Integer> {

    @Query(value = "select * \n" +
            "from Component\n" +
            "where productID = :id", nativeQuery = true)
    List<Component> getComponentByProductID(@Param("id") String id);
}