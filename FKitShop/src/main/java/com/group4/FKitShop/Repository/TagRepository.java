package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    boolean existsByTagName(String name);
}
