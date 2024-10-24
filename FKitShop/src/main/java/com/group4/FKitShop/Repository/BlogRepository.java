package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {

    boolean existsByBlogName(String name);

    // get blog by tagid
    @Query(value = "select b.* from Blog b\n" +
            "join BlogTag bt on b.blogID = bt.blogID\n" +
            "join Tag t on t.tagID = bt.tagID\n" +
            "where t.tagID = :id", nativeQuery = true)
    List<Blog> getBlogList(@Param("id") int id);

    // get blog desc by create date
    @Query(value = "select * from Blog\n" +
            "    order by createDate desc", nativeQuery = true)
    List<Blog> getBlogListDesc();

    // get blog asc by create date
    @Query(value = "select * from Blog\n" +
            "    order by createDate asc", nativeQuery = true)
    List<Blog> getBlogListAsc();

    // get blog active (active blog has status represent by toDelete column, where toDelete = 1
    @Query(value = "select * from Blog\n" +
            "    where toDelete = 1 ", nativeQuery = true)
    List<Blog> getBlogActive();

    // delete by changing status
    @Modifying
    @Query(value = "update Blog\n" +
            "set toDelete = 0\n" +
            "where blogID = :id", nativeQuery = true)
    int deleteStatus(@Param("id") String id);
}
