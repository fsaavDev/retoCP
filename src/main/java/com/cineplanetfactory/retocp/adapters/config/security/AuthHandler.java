package com.cineplanetfactory.retocp.adapters.config.security;

import com.cineplanetfactory.retocp.domain.response.RetoCpApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class AuthHandler implements AuthenticationEntryPoint, Serializable, AccessDeniedHandler {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exceptionMsg = (String) request.getAttribute("exception");

        if(exceptionMsg == null){
            exceptionMsg = "Token not found";
        }

        RetoCpApiResponse<String> res = new RetoCpApiResponse<>(exceptionMsg);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(convertObjectToJson(res));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper.writeValueAsString(object);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String exceptionMsg = (String) request.getAttribute("exception");

        if(exceptionMsg == null){
            exceptionMsg = "User not allowed for this operation";
        }
        RetoCpApiResponse<String> res = new RetoCpApiResponse<>(exceptionMsg);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(convertObjectToJson(res));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
