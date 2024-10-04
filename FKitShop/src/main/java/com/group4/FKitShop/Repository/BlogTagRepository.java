package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.BlogTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogTagRepository extends JpaRepository<BlogTag, Integer>{

    List<BlogTag> findByBlogID(String id);
    List<BlogTag> findByTagID(int id);
}
