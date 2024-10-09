package com.group4.FKitShop.Repository;

import com.amazonaws.services.datapipeline.model.EvaluateExpressionRequest;
import com.group4.FKitShop.Entity.Supporting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportingRepository extends JpaRepository<Supporting, Integer> {

    @Query(value = "select * \n" +
            "from Supporting\n" +
            "where status = :status", nativeQuery = true)
    List<Supporting> findStatusDone(@Param("status") int status);

    @Query(value = "select * \n" +
            "from Supporting\n" +
            "where status = :status and accountID = :accountID", nativeQuery = true)
    List<Supporting> findSupportByAccount(@Param("accountID") String accountID, @Param("status") int status);
}
