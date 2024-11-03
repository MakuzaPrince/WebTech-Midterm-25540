package com.webtech.admissionApp.repository;

import com.webtech.admissionApp.model.ResetToken;
import com.webtech.admissionApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    void deleteByToken(String token);
    Optional<ResetToken> findByUser(User user);
    Optional<ResetToken> findByToken(String token);
}
