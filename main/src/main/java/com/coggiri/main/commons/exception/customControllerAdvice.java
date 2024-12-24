package com.coggiri.main.commons.exception;

import com.coggiri.main.commons.Enums.ErrorType;
import com.coggiri.main.commons.response.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class customControllerAdvice {
    /*
     * 400 Bad request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e){
        Errors errors = e.getBindingResult();
        Map<String, String> validationDetails = new HashMap<>();

        for(FieldError error : errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s",error.getField());
            validationDetails.put(validKeyName,error.getDefaultMessage());
        }

        return new ResponseEntity<>(CustomResponse.error(ErrorType.REQUEST_VALIDATION,validationDetails),e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse<?>> handlerMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e){
        return new ResponseEntity<>(CustomResponse.error(ErrorType.INVALID_TYPE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<CustomResponse<?>> handlerMissingRequestHeaderException(final MissingRequestHeaderException e){
        return new ResponseEntity<>(CustomResponse.error(ErrorType.INVALID_MISSING_HEADER),e.getStatusCode());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse<?>> handlerHttpMessageNotReadableException(final  HttpMessageNotReadableException e){
        return new ResponseEntity<>(CustomResponse.error(ErrorType.INVALID_HTTP_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomResponse<?>> handlerHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e){
        return new ResponseEntity<>(CustomResponse.error(ErrorType.METHOD_NOT_ALLOWED),e.getStatusCode());
    }

    /*
     * 500 INTERNAL_SERVER
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<?>> handlerException(final Exception e, final HttpServletRequest request) throws IOException{
        return new ResponseEntity<>(CustomResponse.error(ErrorType.INTERNAL_SERVER),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse<?>> handlerIllegalArgumentException(final IllegalArgumentException e, final  HttpServletRequest request){
        return new ResponseEntity<>(CustomResponse.error(ErrorType.INTERNAL_SERVER),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<CustomResponse<?>> handlerIOException(final IOException e, final HttpServletRequest request) {
        return new ResponseEntity<>(CustomResponse.error(ErrorType.INTERNAL_SERVER),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomResponse<?>> handlerRuntimeException(final RuntimeException e, final HttpServletRequest request){
        return new ResponseEntity<>(CustomResponse.error(ErrorType.INTERNAL_SERVER),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * CUSTOM_ERROR
     */

    @ExceptionHandler(customException.class)
    public ResponseEntity<CustomResponse<?>> handlerCustomException(customException e){
        return new ResponseEntity<>(CustomResponse.error(e.getErrorType()), HttpStatusCode.valueOf(e.getHttpStatus()));
    }
}
