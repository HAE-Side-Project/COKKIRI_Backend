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
public class customResponse<T>{
    private final int status;
    private final int code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static customResponse<?> success(SuccessType successType){
        return new customResponse<>(successType.getHttpStatusCode(),successType.getCode(),successType.getMessage());
    }

    public static <T> customResponse<T> success(SuccessType successType, T data){
        return new customResponse<>(successType.getHttpStatusCode(),successType.getCode(),successType.getMessage(),data);
    }

    public static customResponse<?> error(ErrorType errorType){
        return new customResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),errorType.getMessage());
    }

    public static <T> customResponse<T> error (ErrorType errorType, T data){
        return new customResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),errorType.getMessage(),data);
    }

    public static customResponse<?> error(ErrorType errorType, String message){
        return new customResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),message);
    }

    public static <T> customResponse<T> error(ErrorType errorType,String message, T data){
        return new customResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),message,data);
    }

    public static <T> customResponse<Exception> error(ErrorType errorType, Exception e){
        return new customResponse<>(errorType.getHttpStatusCode(),errorType.getCode(),errorType.getMessage(),e);
    }
}
