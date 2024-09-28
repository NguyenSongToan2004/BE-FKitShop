package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {
    boolean existsByBlogName(String name);
}
