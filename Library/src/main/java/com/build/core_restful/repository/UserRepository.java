package com.build.core_restful.repository;

import com.build.core_restful.domain.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Long countByStatus(String status);

    User findByEmail(String email);

    List<User> findByStatus(String status);

    User findByEmailAndRefreshToken(String email, String refreshToken);

    boolean existsByEmail(String email);

    Optional<User> findByIdAndStatus(Long id, String statusUser);
}
