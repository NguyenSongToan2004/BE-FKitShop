package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    boolean existsByCategoryName(String name);

    // get category by productID
    @Query(value = "select c.*\n" +
            " from Category c\n" +
            "join CateProduct cp on c.categoryID = cp.categoryID\n" +
            "join StemProduct st on cp.productID = st.productID\n" +
            "where st.productID = :id ", nativeQuery = true)
    List<Category> getCategoryList(@Param("id") String id);

    // get category by tagid
    @Query(value = "select * \n" +
            "from Category\n" +
            "where tagID = :id", nativeQuery = true)
    List<Category> getCategoryByTagID(@Param("id") int id);

    // get cate active (active cate has status = 1
    @Query(value = "select * \n" +
            "from Category\n" +
            "where status = 1", nativeQuery = true)
    List<Category> getCategoryActive();

    // get cate by character(s) contained in cate name
    @Query(value = "SELECT * FROM Category\n" +
            "where categoryName like :character", nativeQuery = true)
    List<Category> getCategoryByName(@Param("character") String character);


    // delete by changing status
    @Modifying
    @Query(value = "update Category\n" +
            "set status = 0\n" +
            "where categoryID = :id", nativeQuery = true)
    int deleteStatus(@Param("id") String id);
}
