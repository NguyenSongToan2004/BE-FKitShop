package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query(value = "select * from Feedback\n" +
            "    where accountID = :id", nativeQuery = true)
    List<Feedback> getFeedbackByAccountID(@Param("id") String id);

    @Query(value = "select * from Feedback\n" +
            "    where productID = :id", nativeQuery = true)
    List<Feedback> getFeedbackByProductID(@Param("id") String id);

}
