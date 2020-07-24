package com.sample.config;

import com.sample.component.JwtConst;
import com.sample.component.JwtUtils;
import com.sample.domain.AccessToken;
import com.sample.domain.Account;
import com.sample.service.AccessTokenService;
import com.sample.service.AccountService;
import com.sample.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 인터셉터에서 jwt 토큰 처리 로직 구현
 * 1. 로그인
 */
@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("Authorization 처리");
        // Token 확인 -> AccessToken
        String accessToken = request.getHeader("Authorization")
                .replace("Bearer ", "");

        if(!jwtUtils.isValidToken(accessToken)) {

            log.error("[JWT ExpiredJwtException] 발생 -> AccessToken 재발행 프로세스 시작 !!");

            AccessToken redisAccessToken = accessTokenService.get(accessToken);
            log.error("[JWT ExpiredJwtException] Redis에서 AccessToken 조회 : {}", redisAccessToken);

            if(redisAccessToken.getAccessToken().equals(accessToken)) { // AccessToken 비교 (Request vs Redis Server)
                log.error("[JWT ExpiredJwtException] 정상 만료된 토큰임을 확인");

                String refreshToken = refreshTokenService.getRefreshTokenByUserName(redisAccessToken.getUserName());
                log.error("[JWT ExpiredJwtException] DB 조회하여 RefreshToken 조회 후 유효성 검사 : {}", refreshToken);

                if(jwtUtils.isValidToken(refreshToken)) {

                    String userName = jwtUtils.getUserNameFromToken(refreshToken);
                    Account account = accountService.getAccountByUserName(userName);
                    log.error("[JWT ExpiredJwtException] 유효한 RefreshToken 임이 확인되어 AccessToken을 재발행하기 위한 사용자 정보 조회 : {}", account);

                    String newAccessToken = jwtUtils.generateToken(account, JwtConst.ACCESS_EXPIRED);
                    log.error("[JWT ExpiredJwtException] 새로운 AccessToken 발행 : {}", newAccessToken);
                    /* accessToken 재갱신 */
                    accessTokenService.update(account.getUserName(), newAccessToken);
                    response.addHeader("Authorization", "Bearer " + newAccessToken);
                } else {
                    /* 리프레시 토큰의 오류인 경우 -> 로그인 필요 */
                    return false;
                }

            } else { // redis에 서로 다른 디바이스의 accessToken이 있을 수 있음

            }

        }

        log.info("권한에 따른 로직 필요");

        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
}
