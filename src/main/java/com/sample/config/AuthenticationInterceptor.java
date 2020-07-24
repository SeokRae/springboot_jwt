package com.sample.config;

import com.sample.component.JwtUtils;
import com.sample.service.AccountService;
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
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!"POST".equalsIgnoreCase(request.getMethod())) {
            // method 방식이 POST가 아닌 경우 유효하지 않은 요청 방식

            log.error("POST 메서드 요청이 아니면 토큰을 발행 받을 수 없음");
        }
        String name = request.getParameter("userName");
        String pw = request.getParameter("userPw");

        String token = jwtUtils.generateJwtToken(accountService.get(name, pw));

        ModelAndView model = new ModelAndView();
        model.addObject("token", token);

        postHandle(request, response, handler, model);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        assert modelAndView != null;

        Map<String, Object> modelMap = modelAndView.getModel();
        String token = (String) modelMap.get("token");
        response.addHeader("Authorization", "Bearer " + token);
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
