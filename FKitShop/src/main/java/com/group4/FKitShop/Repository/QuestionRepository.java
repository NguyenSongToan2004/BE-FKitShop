package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Question;
import com.group4.FKitShop.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // get question by accountid
    @Query(value = "select * from Question\n" +
            "    where accountID = :id", nativeQuery = true)
    List<Question> getQuestionByAccountID(@Param("id") String id);

    // get question by labid
    @Query(value = "select * from Question\n" +
            "    where labID = :id", nativeQuery = true)
    List<Question> getQuestionByLabID(@Param("id") String id);

    // get question by accountid and labid
    @Query(value = "select * from Question where accountID = :accountID and labID = :labID", nativeQuery = true)
    List<Question> getQuesByAccountAndLab(@Param("accountID") String accountID, @Param("labID") String labID);

    // get question by status
    @Query(value = "select * from Question\n" +
            "    where status = :st", nativeQuery = true)
    List<Question> getQuestionByStatus(@Param("st") int st);
}
