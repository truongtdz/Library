package com.build.core_restful.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.build.core_restful.domain.DataSave;

@Repository
public interface DataSaveRepository extends JpaRepository<DataSave, Long>{
    // Trong DataSaveRepository cần có:
    @Query("SELECT d FROM DataSave d WHERE d.gender = :gender AND d.city = :city AND :age BETWEEN d.startAge AND d.endAge")
    List<DataSave> findByGenderAndCityAndAgeRange(@Param("gender") String gender, 
                                                @Param("city") String city, 
                                                @Param("age") Long age);

    @Query("SELECT d FROM DataSave d WHERE d.gender = :gender AND :age BETWEEN d.startAge AND d.endAge")
    List<DataSave> findByGenderAndAgeRange(@Param("gender") String gender, 
                                        @Param("age") Long age);
}
