package com.group4.FKitShop.Repository;

import com.group4.FKitShop.Entity.LabGuide;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabGuideRepository extends JpaRepository<LabGuide, Integer> {

    // Lấy ra step number hiện tại
    @Query(value = "select step\n" +
            "from LabGuide\n" +
            "where labID = :id\n" +
            "order by step desc\n" +
            "limit 1", nativeQuery = true)
    Optional<Integer> getStepNumber(String id);

    // Lấy những lab guide thuộc 1 LabID cụ thể
    // List<LabGuide> findByLabID(String id);


    // Lấy những lab guild có stepNumber lớn hơn để xóa
    @Query(value = "select * \n" +
            "from LabGuide\n" +
            "where labID = :labID and step > :stepDelete\n" +
            "order by step desc", nativeQuery = true)
    List<LabGuide> findByLabID(@Param("labID") String id, @Param("stepDelete") int step);

    @Modifying
    @Transactional
    @Query (value = "update LabGuide\n" +
            "set isUsed = 0\n" +
            "where labID = :labID and labGuideID not in (:labGuideIDs)", nativeQuery = true)
    void updateLabGuide(@Param("labID") String labID, @Param("labGuideIDs") List<Integer> labGuideIDs);

}
