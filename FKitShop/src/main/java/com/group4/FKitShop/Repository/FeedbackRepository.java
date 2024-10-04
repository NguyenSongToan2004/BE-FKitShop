package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
