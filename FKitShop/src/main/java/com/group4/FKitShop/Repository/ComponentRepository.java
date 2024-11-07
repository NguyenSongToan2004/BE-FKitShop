package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Integer> {

    List<Component> findByProductID(String id);
}