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

    @Query(value = "select *\n" +
            "from StemProduct\n" +
            "order by unitOnOrder desc\n" +
            "limit 8", nativeQuery = true)
    List<Product> getHotProducts();

    @Query(value = "select stem.*\n" +
            "from StemProduct stem join CateProduct cate\n" +
            "\ton stem.productID = cate.productID\n" +
            "where cate.categoryID = :cateID\n" +
            "order by price asc", nativeQuery = true)
    List<Product> getPriceAscProducts(@Param("cateID") String cateID);

    @Query(value = "select stem.*\n" +
            "from StemProduct stem join CateProduct cate\n" +
            "\ton stem.productID = cate.productID\n" +
            "where cate.categoryID = :cateID\n" +
            "order by price desc", nativeQuery = true)
    List<Product> getPriceDescProducts(@Param("cateID") String cateID);

    @Query(value = "select true\n" +
            "from StemProduct s left join Image i on s.productID = i.productID\n" +
            "where s.productID = :productID and i.url like :url", nativeQuery = true
    )
    String existsByUrl(@Param("productID") String productID, @Param("url") String url);
    @Query(value = "select st.* from StemProduct st\n" +
            "    join CateProduct cp on cp.productID = st.productID\n" +
            "    join Category c on c.categoryID = cp.categoryID\n" +
            "    where c.categoryID = :id", nativeQuery = true)
    List<Product> getProductIDList(@Param("id") String id);

    @Query(value = "select stem.*\n" +
        "from StemProduct stem join CateProduct cate\n" +
        "\t on stem.productID = cate.productID\n" +
        "where cate.categoryID = :cateID", nativeQuery = true)
    List<Product> getProductByCate(@Param("cateID") String cateID);

    @Query(value = "select *\n" +
        "from StemProduct\n" +
        "where name like :name", nativeQuery = true)
    List<Product> getByName(@Param("name") String name);

    List<Product> getProductsByType(String type);

    @Query(value = "SELECT productID, SUM(quantity) AS totalSold\n" +
            "FROM OrderDetails\n" +
            "GROUP BY productID\n" +
            "ORDER BY totalSold DESC;", nativeQuery = true)
    List<Object> getHotProduct();
}
