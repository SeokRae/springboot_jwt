package com.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor();
    }

    @Bean
    public HandlerInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry
                .addInterceptor(authenticationInterceptor())
                .addPathPatterns(
                        "/auth/login/**"        /* 로그인 시 인증 처리                          */
                )
                .excludePathPatterns(
                        "/account/add/**"       /* 사용자 등록 시 인증 및 권한 필요 없음           */
                        , "/account/all"        /* 사용자 조회 시 인증 및 권한 필용 없음           */
                );
        registry
                .addInterceptor(authorizationInterceptor())
                .addPathPatterns(
                        "/auth/user"
                        , "/auth/admin"
                        , "/account/update/**"

                ) // 권한에 따라 접근 가능하도록 로직 처리
                .excludePathPatterns(
                        "/account/add/**"       /* 사용자 등록 시 인증 및 권한 필요 없음             */
                        , "/account/all"        /* 사용자 조회 시 인증 및 권한 필용 없음             */
                        , "/auth/login/**"      /* 사용자 로그인 페이지 접근 시 권한 확인 필요 없음     */
                        , "/account/update/**"  /* 사용자 정보 업데이트 URL 호출                   */
                );
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
