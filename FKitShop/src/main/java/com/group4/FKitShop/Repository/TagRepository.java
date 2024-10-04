package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.Blog;
import com.group4.FKitShop.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    boolean existsByTagName(String name);

    @Query(value = "SELECT t.* FROM Tag t \n" +
            "join BlogTag bt on bt.tagID = t.tagID\n" +
            "join Blog b on b.blogID = bt.blogID\n" +
            "where b.blogID = :id", nativeQuery = true)
    List<Tag> getTagList(@Param("id") String id);
}
