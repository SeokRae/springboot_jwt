package com.sample.config;

import com.sample.component.JwtConst;
import com.sample.component.JwtUtils;
import com.sample.component.StringUtils;
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
import java.util.Enumeration;
import java.util.Map;

@Slf4j
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!"POST".equalsIgnoreCase(request.getMethod())) {
            // method 방식이 POST가 아닌 경우 유효하지 않은 요청 방식
            log.error("POST 메서드 요청이 아니면 토큰을 발행 받을 수 없음");
        }
        // form 파라미터 확인
        String name = StringUtils.getOrDefault(request.getParameter("userName"), "");
        String pw = StringUtils.getOrDefault(request.getParameter("userPw"), "");

        // 사용자 확인 및 토큰 발급
        Account account = accountService.get(name, pw);


        // 여기까지 왔으면 성공 그전에 예외처리가 되어야 함
        ModelAndView model = new ModelAndView();
        model.addObject("account", account);

        postHandle(request, response, handler, model);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        assert modelAndView != null;

        // 사용자 조회
        Map<String, Object> modelMap = modelAndView.getModel();
        Account account = (Account) modelMap.get("account");

        /* 엑세스 토큰, 리프레시 토큰 발급 */
        String accessToken = createTokens(account);
        response.addHeader("Authorization", "Bearer " + accessToken);

    }

    /**
     * 엑세스 / 리프레시 토큰 발급
     * 1. 엑세스 토큰 발환
     * 2. 리프레시 토큰 DB 저장
     * @param account request 로 DB 조회한
     * @return
     */
    private String createTokens(Account account) {
        // DB 조회 시 오류 발생 처리 필요
        String userName = account.getUserName();
        // 엑세스 토큰 발급 및 헤더 저장
        String accessToken = jwtUtils.generateToken(account, JwtConst.ACCESS_EXPIRED);
        accessTokenService.add(userName, accessToken);
        // 리프레시 토큰 발급 및 DB 저장
        String refreshToken = jwtUtils.generateToken(account, JwtConst.REFRESH_EXPIRED);
        refreshTokenService.add(userName, refreshToken);

        return accessToken;
    }

    private void getParameters(HttpServletRequest request) {
        log.info("==============================================getParameters========================================================");
        Enumeration<String> e = request.getParameterNames();
        while(e.hasMoreElements()) {
            String name = e.nextElement();
            System.out.println(name + "\t" + request.getParameter(name));
        }
        log.info("==============================================getParameters========================================================");
    }
}
