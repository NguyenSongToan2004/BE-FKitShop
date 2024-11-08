package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findByAccountID(String id);

    List<Feedback> findByProductID(String id);

}
