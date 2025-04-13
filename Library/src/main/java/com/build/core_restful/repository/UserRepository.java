package com.build.core_restful.repository;

import com.build.core_restful.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByEmailAndRefreshToken(String email, String refreshToken);

    boolean existsByEmail(String email);

    Page<User> findByActive(boolean active, Pageable pageable);

    User findByIdAndActive(Long id, boolean active);
}
