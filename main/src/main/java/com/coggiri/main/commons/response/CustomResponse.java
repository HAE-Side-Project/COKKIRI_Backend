package com.coggiri.main.commons.response;

import com.coggiri.main.commons.Enums.ErrorType;
import com.coggiri.main.commons.Enums.SuccessType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonPropertyOrder({"status","code","message","data"})
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomResponse<T>{
    private final int status;
    private final int code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static CustomResponse<?> success(SuccessType successType){
        return new CustomResponse<>(successType.getHttpStatusCode(),successType.getCode(),successType.getMessage());
    }

    public static <T> CustomResponse<T> success(SuccessType successType, T data){
        return new CustomResponse<>(successType.getHttpStatusCode(),successType.getCode(),successType.getMessage(),data);
    }

    public static CustomResponse<?> error(ErrorType errorType){
        return new CustomResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),errorType.getMessage());
    }

    public static <T> CustomResponse<T> error (ErrorType errorType, T data){
        return new CustomResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),errorType.getMessage(),data);
    }

    public static CustomResponse<?> error(ErrorType errorType, String message){
        return new CustomResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),message);
    }

    public static <T> CustomResponse<T> error(ErrorType errorType, String message, T data){
        return new CustomResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),message,data);
    }

    public static <T> CustomResponse<Exception> error(ErrorType errorType, Exception e){
        return new CustomResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),errorType.getMessage(),e);
    }
}
