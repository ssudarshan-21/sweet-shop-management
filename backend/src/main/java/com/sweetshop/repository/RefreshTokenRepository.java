package com.sweetshop.repository;

import com.sweetshop.entity.RefreshToken;
import com.sweetshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByUser(User user);
    
    void deleteAllByUser(User user);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    int deleteByExpiryDateBefore(@Param("now") LocalDateTime now);
    
    @Modifying
    @Transactional
    int deleteByUser(User user);
    
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.expiryDate < :now")
    java.util.List<RefreshToken> findExpiredTokens(@Param("now") LocalDateTime now);
}