package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "select url\n" +
            "from Image\n" +
            "where url like :fileName\n" +
            "limit 1", nativeQuery = true)
    String existFileName(@Param("fileName") String fileName);
}
