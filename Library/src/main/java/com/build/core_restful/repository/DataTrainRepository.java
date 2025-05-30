package com.build.core_restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.build.core_restful.domain.DataTrain;

@Repository
public interface DataTrainRepository extends JpaRepository<DataTrain, Long>{
    
}
