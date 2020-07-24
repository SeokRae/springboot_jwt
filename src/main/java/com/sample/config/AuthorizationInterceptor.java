package com.sample.config;

import com.sample.component.JwtConst;
import com.sample.component.JwtUtils;
import com.sample.domain.access.AccessToken;
import com.sample.domain.account.Account;
import com.sample.service.AccessTokenService;
import com.sample.service.AccountService;
import com.sample.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            String redisToken = redisAccessToken.getAccessToken();
            String redisUserName = redisAccessToken.getUserName();
            log.error("[JWT ExpiredJwtException] Redis에서 AccessToken 조회 : {}", redisToken);

            if(redisToken.equals(accessToken)) { // AccessToken 비교 (Request vs Redis Server)
                log.error("[JWT ExpiredJwtException] 정상 만료된 토큰임을 확인");

                String refreshToken = refreshTokenService.getRefreshTokenByUserName(redisUserName);
                log.error("[JWT ExpiredJwtException] DB 조회하여 RefreshToken 조회 후 유효성 검사 : {}", refreshToken);

                if(jwtUtils.isValidToken(refreshToken)) {
                    log.error("[JWT ExpiredJwtException] RefreshToken 유효성 확인 -> AccessToken 재발급 시작");
                    String newAccessToken = reAccessToken(refreshToken);
                    response.addHeader("Authorization", "Bearer " + newAccessToken);

                } else {
                    /* 리프레시 토큰의 오류인 경우 -> 로그인 필요 */
                    log.error("[JWT RefreshTokenException] 리프레시 토큰 만료 -> 자원 접근 불가 (로그인 필요)");
                    return false;
                }

            } else { // redis에 서로 다른 디바이스의 accessToken이 있을 수 있음

            }

        }

        log.info("권한에 따른 로직 필요");
        return true;
    }

    /* AccessToken 재발급 로직 */
    private String reAccessToken(String refreshToken) {

        String userName = jwtUtils.getUserNameFromToken(refreshToken);
        Account account = accountService.getAccountByUserName(userName);
        log.error("[JWT ExpiredJwtException] AccessToken을 재발행하기 위한 사용자 정보 조회 : {}", account);

        String newAccessToken = jwtUtils.generateToken(account, JwtConst.ACCESS_EXPIRED);
        log.error("[JWT ExpiredJwtException] 새로운 AccessToken 발행 : {}", newAccessToken);

        /* accessToken 재갱신 */
        return accessTokenService.update(account.getUserName(), newAccessToken);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
}
