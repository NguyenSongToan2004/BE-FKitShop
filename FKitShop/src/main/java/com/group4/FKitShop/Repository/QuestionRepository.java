package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
