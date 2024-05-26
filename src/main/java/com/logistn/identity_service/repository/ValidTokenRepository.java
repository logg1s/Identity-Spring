package com.logistn.identity_service.repository;

import com.logistn.identity_service.entity.ValidToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidTokenRepository extends JpaRepository<ValidToken, String> {
    Optional<ValidToken> findByRefreshTokenId(String token);

    Optional<ValidToken> findByPrevTokenId(String token);
}
