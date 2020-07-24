package com.sample.config;

import com.sample.component.JwtUtils;
import com.sample.service.AccountService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * 인터셉터에서 jwt 토큰 처리 로직 구현
 * 1. 로그인
 */
@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    AccountService accountService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Authorization 처리");
        /* 데이터 확인 */
        get(request);
        getHeaders(request);
        getParameters(request);
//        getBody(request);


        // Token 확인 -> AccessToken
        String accessToken = request.getHeader("Authorization")
                .replace("Bearer ", "");

        // Token의 자원은 Entity:userName, Token:id 으로 관리
        if(!jwtUtils.isValidToken(accessToken)) {
            // expired 된 Jwt이기 때문에 Refresh Token 확인 후에 재발급 하던지
            log.error("JWT Error");
            // redis에 token 체크

            // 있으면 -> accessToken 비교 ?

            // 없으면 -> refreshToken 조회

            // refreshToken 있으면 -> 재발급

            // refreshToken 없으면 -> 재 로그인 필요, login page redirect 필요

            // 정상 토큰이므로 필요한 resources에 접근 가능한 권한이 있는지 확인
            Claims claims = jwtUtils.getClaims(accessToken);
            String id = (String) claims.get("id");

            log.info("접근 주체 : {}", id);
            log.info("토큰 유효기간 : {}", claims.getExpiration());

        }

        String role = (String) jwtUtils.getClaims(accessToken).get("role");
        if("USER".equals(role)) {

        } else if("ADMIN".equals(role)){

        }

        return true;
    }

    private void get(HttpServletRequest request) throws IOException, ServletException {
        System.out.println("getContextPath : " + request.getContextPath());
        System.out.println("getAuthType : " + request.getAuthType());
        System.out.println(request.getCookies());
//        System.out.println(request.changeSessionId());
        System.out.println(request.getHttpServletMapping());
        System.out.println(request.getMethod());
//        System.out.println(request.getParts());
        System.out.println(request.getPathInfo());
        System.out.println(request.getQueryString());
        System.out.println(request.getPathTranslated());
        System.out.println(request.getRemoteUser());
        System.out.println(request.getRequestedSessionId());
        System.out.println(request.getRequestURI());
        System.out.println(request.getRequestURL());
        System.out.println(request.getServletPath());
        System.out.println(request.getTrailerFields());
        System.out.println(request.getUserPrincipal());
//        System.out.println(request.getAsyncContext());
        System.out.println(request.getCharacterEncoding());
        System.out.println(request.getContentLength());
        System.out.println(request.getContentLengthLong());
        System.out.println(request.getContentType());
        System.out.println(request.getDispatcherType());
        System.out.println(request.getLocalAddr());
        System.out.println(request.getLocalName());
        System.out.println(request.getLocalPort());
        System.out.println(request.getProtocol());
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getRemoteHost());
        System.out.println(request.getRemotePort());
        System.out.println(request.getScheme());
        System.out.println(request.getServerName());
        System.out.println(request.getServerPort());
        System.out.println(request.getServletContext());
        System.out.println(request.getLocale());
        HttpSession session = request.getSession();
        System.out.println(session.getId());

    }

    private String getBody(HttpServletRequest request) throws IOException {
        log.info("==============================================getBody========================================================");
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        body = stringBuilder.toString();
        System.out.println(body);
        log.info("==============================================getBody========================================================");
        return body;
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

    /* header name 출력 */
    private void getHeaders(HttpServletRequest request) {
        Enumeration<String> e = request.getHeaderNames();
        log.info("==============================================getHeaders============================================================");
        while(e.hasMoreElements()) {
            String name = e.nextElement();
            System.out.println(name + "\t" + request.getHeader(name));
        }
        log.info("==============================================getHeaders============================================================");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        System.out.println("post handle");
        if("/account/add/seok/1234".equals(request.getRequestURI())) {
            response.setHeader("Authorization", "Bearer token");
        }
    }
}
