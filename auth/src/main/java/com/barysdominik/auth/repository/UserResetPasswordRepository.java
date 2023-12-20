package com.barysdominik.auth.repository;

import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.entity.user.UserResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserResetPasswordRepository extends JpaRepository<UserResetPassword, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM user_reset_password where created_at <= current_timestamp - INTERVAL '15 minutes'"
    )
    List<UserResetPassword> findExpiredOperations();
    Optional<UserResetPassword> findByUuid(String uuid);
    @Modifying
    void deleteAllByUser(User user);
}
