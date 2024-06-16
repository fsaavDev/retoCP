package com.cineplanetfactory.retocp.adapters.web.exception;

import com.cineplanetfactory.retocp.domain.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class Handler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericExceptions(Exception e, WebRequest req){
        System.out.println(e.toString());
        ApiResponse<String> res = new ApiResponse<>(e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleModelNotFoundException(ModelNotFoundException e, WebRequest req){
        ApiResponse<String> res = new ApiResponse<>(e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleSQLExceptions(DataIntegrityViolationException e, WebRequest req){
        List<String> errList = e.getMostSpecificCause().getMessage().lines().map(String::strip).collect(Collectors.toList());
        ApiResponse<List<String>> res = new ApiResponse<>(errList);
        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers, HttpStatus status, WebRequest req) {
        ApiResponse<String> res = new ApiResponse<>(e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest req) {
        Map<String,String> errList = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(err-> errList.put(err.getField(),err.getDefaultMessage()));
        ApiResponse<Map<String,String>> res = new ApiResponse<>(errList);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponse<String> res = new ApiResponse<>(e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}