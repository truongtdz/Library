package com.build.core_restful.repository;

import com.build.core_restful.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByEmailAndRefreshToken(String email, String refreshToken);

    boolean existsByEmail(String email);
}
