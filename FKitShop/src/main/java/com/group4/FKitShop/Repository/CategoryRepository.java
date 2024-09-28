package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    boolean existsByCategoryName(String name);
}
