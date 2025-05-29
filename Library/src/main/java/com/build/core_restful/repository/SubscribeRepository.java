package com.build.core_restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.build.core_restful.domain.Subscribe;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long>{
    void deleteByEmail(String email);
}
