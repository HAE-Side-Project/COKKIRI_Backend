package com.coggiri.main.commons.exception;

import com.coggiri.main.commons.Enums.ErrorType;
import lombok.Getter;

@Getter
public class customException extends RuntimeException{
    private final ErrorType errorType;

    public customException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public int getHttpStatus(){
        return errorType.getHttpStatusCode();
    }
}
