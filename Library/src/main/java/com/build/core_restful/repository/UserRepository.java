package com.build.core_restful.repository;

import com.build.core_restful.domain.User;
import com.build.core_restful.util.enums.UserStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByEmailAndRefreshToken(String email, String refreshToken);

    boolean existsByEmail(String email);

    Page<User> findByStatus(UserStatusEnum statusUser, Pageable pageable);

    User findByIdAndStatus(Long id, UserStatusEnum statusUser);
}
