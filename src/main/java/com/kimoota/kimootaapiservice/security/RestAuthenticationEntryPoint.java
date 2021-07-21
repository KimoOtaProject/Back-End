package com.kimoota.kimootaapiservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 엔트리 포인트 - 사용자 인증 없이 리소스 접근 시 오류 출력
public class RestAuthenticationEntryPoint extends AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        logger.error("인증되지 않는 접근에 대한 에러. msg - {}", authException.getMessage());

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());

    }
}
