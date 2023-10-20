package com.barysdominik.auth.repository;

import com.barysdominik.auth.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    @Query(nativeQuery = true, value = "SELECT * FROM users where username=?1 and islock=false and isenabled=true")
    Optional<User> findNonLockedAndEnabledUserByUsername(String username);
    Optional<User> findUserByUuid(String uuid);
}
