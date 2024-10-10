package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LabRepository extends JpaRepository<Lab, String> {
    boolean existsByLabID(String id);

    @Query(value = "select labID\n" +
            "from Lab\n" +
            "order by labID desc\n" +
            "limit 1", nativeQuery = true)
    String getNumberLab();

    boolean existsByName(String name);

    List<Lab> findByProductID(String productID);
}
