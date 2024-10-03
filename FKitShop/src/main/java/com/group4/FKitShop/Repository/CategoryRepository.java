package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    boolean existsByCategoryName(String name);

    // filter for latest product
    @Query(value = "select c.categoryID from Category c\n" +
            "join CateProduct cp on c.categoryID = cp.categoryID\n" +
            "join StemProduct st on cp.productID = st.productID\n" +
            "where st.productID = :id ", nativeQuery = true)
    List<String> getCategoryIDList(@Param("id") String id);
}
