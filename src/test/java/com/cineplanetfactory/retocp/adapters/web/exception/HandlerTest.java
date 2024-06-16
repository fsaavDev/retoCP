package com.cineplanetfactory.retocp.adapters.web.exception;

import com.cineplanetfactory.retocp.domain.response.RetoCpApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandlerTest {

    @InjectMocks
    private Handler handler;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private WebRequest getWebRequest() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletWebRequest(servletRequest));
        return new ServletWebRequest(servletRequest);
    }

    @Test
    void handleGenericExceptions() {
        //GIVEN
        Exception e = new Exception("Error genérico");
        WebRequest request = getWebRequest();

        //WHEN
        ResponseEntity<RetoCpApiResponse<String>> response = handler.handleGenericExceptions(e, request);


        //THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error genérico", response.getBody().getData());
    }

    @Test
    void handleModelNotFoundException() {
        //GIVEN
        ModelNotFoundException e = new ModelNotFoundException("Entidad no encontrada");
        WebRequest request = getWebRequest();

        //WHEN
        ResponseEntity<RetoCpApiResponse<String>> response = handler.handleModelNotFoundException(e, request);

        //THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Entidad no encontrada", response.getBody().getData());
    }

    @Test
    void handleUnauthorizedException() {
        //GIVEN
        UnauthorizedException e = new UnauthorizedException("Error de autorización",null);
        WebRequest request = getWebRequest();

        //WHEN
        ResponseEntity<RetoCpApiResponse<String>> response = handler.handleUnauthorizedException(e, request);

        //THEN
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error de autorización", response.getBody().getData());
    }

    @Test
    void handleSQLExceptions() {
        //GIVEN
        DataIntegrityViolationException e = new DataIntegrityViolationException("Error SQL");
        WebRequest request = getWebRequest();

        //WHEN
        ResponseEntity<RetoCpApiResponse<List<String>>> response = handler.handleSQLExceptions(e, request);

        //THEN
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error SQL", response.getBody().getData().get(0));
    }

    @Test
    void handleNoHandlerFoundException() {
        //GIVEN
        NoHandlerFoundException e = new NoHandlerFoundException("GET", "/test", new HttpHeaders());
        WebRequest request = getWebRequest();

        //WHEN
        ResponseEntity<Object> response = handler.handleNoHandlerFoundException(e, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

        //THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof RetoCpApiResponse);
        assertEquals("No handler found for GET /test", ((RetoCpApiResponse<?>) response.getBody()).getData());
    }

    @Test
    void handleMissingServletRequestParameter() {
        //GIVEN
        MissingServletRequestParameterException e = new MissingServletRequestParameterException("param", "String");
        WebRequest request = getWebRequest();

        //WHEN
        ResponseEntity<Object> response = handler.handleMissingServletRequestParameter(e, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

        //THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof RetoCpApiResponse);
        assertEquals("Required request parameter 'param' for method parameter type String is not present", ((RetoCpApiResponse<?>) response.getBody()).getData());
    }
}