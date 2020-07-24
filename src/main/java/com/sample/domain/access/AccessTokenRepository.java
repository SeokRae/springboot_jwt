package com.sample.domain.access;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    AccessToken findByAccessToken(String accessToken);

    Optional<AccessToken> findByUserName(String userName);
}
