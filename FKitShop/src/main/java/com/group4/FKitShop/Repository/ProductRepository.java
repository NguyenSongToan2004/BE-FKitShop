package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByName(String name);

    // Get the largest ID
    @Query(value = "select productID\n" +
            "from StemProduct\n" +
            "order by productID desc\n" +
            "limit 1", nativeQuery = true)
    String getNumberProduct();

    // delete by changing status
    @Modifying
    @Query(value = "update StemProduct\n" +
            "set status = 'inactive'\n" +
            "where productID = :id", nativeQuery = true)
    int deleteStatus(@Param("id") String id);


    // filter for latest product
    @Query(value = "select *\n" +
            "from StemProduct\n" +
            "where status = 'active'\n" +
            "order by createDate desc\n" +
            "limit 10", nativeQuery = true)
    List<Product> getLatestProduct();


    // Filter for all products with status is active
    @Query(value = "select * \n" +
            "from StemProduct\n" +
            "where status = 'active'", nativeQuery = true)
    List<Product> getActiveProducts();

    @Query(value = "select st.* from StemProduct st\n" +
            "    join CateProduct cp on cp.productID = st.productID\n" +
            "    join Category c on c.categoryID = cp.categoryID\n" +
            "    where c.categoryID = :id", nativeQuery = true)
    List<Product> getProductIDList(@Param("id") String id);
}
