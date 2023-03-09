package com.example.newsns.exception;

import com.example.newsns.controller.response.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//CustomAuthenticationEntryPoint는 사용자가 인증되지 않은 경우에 호출될 커스텀 AuthenticationEntryPoint 클래스입니다.
// 이 클래스에서는 사용자에게 로그인을 유도하는 메시지를 제공하거나, 인증되지 않은 사용자의 요청을 거부하는 등의 작업을 수행할 수 있습니다.
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
        response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.name()).toStream());//이쁘게 보여주기 위해 toStream 쓴다.
    }
}
