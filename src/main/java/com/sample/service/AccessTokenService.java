package com.sample.service;

import com.sample.domain.AccessToken;
import com.sample.domain.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccessTokenService {
    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Transactional
    /* 토큰 저장 */
    public void add(String userName, String accessToken) {
        accessTokenRepository.save(
                AccessToken.builder()
                        .userName(userName)
                        .accessToken(accessToken)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    /* 토큰 조회 */
    public AccessToken get(String accessToken) {
        return accessTokenRepository.findByAccessToken(accessToken);
    }

    @Transactional
    public void update(String userName, String accessToken) {
        accessTokenRepository.findByUserName(userName)
                .map(acToken -> {
                    acToken.updateToken(accessToken);
                    return acToken.getAccessToken();
                })
                .orElseGet(() -> { throw new RuntimeException("데이터가 없습니다."); });
    }
}
