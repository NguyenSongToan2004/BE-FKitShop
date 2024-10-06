package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query(value = "select * from Question\n" +
            "    where accountID = :id", nativeQuery = true)
    List<Question> getQuestionByAccountID(@Param("id") String id);

    @Query(value = "select * from Question\n" +
            "    where labID = :id", nativeQuery = true)
    List<Question> getQuestionByLabID(@Param("id") String id);

}
