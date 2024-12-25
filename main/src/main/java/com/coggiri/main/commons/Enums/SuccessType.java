package com.coggiri.main.commons.Enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessType {
    /*
    *  200 ok (2000 ~ 2099)
    * */
    OK(HttpStatus.OK,2000,"성공"),
    SUCCESS_USER_LOGIN(HttpStatus.OK,2001,"로그인에 성공했습니다."),
    SUCCESS_USER_DELETE(HttpStatus.OK,2002,"회원탈퇴에 성공했습니다."),
    /*
    *  201 create (2100 ~ 2199)
    * */
    SUCCESS_USER_CREATE(HttpStatus.CREATED,2100,"회원가입에 성공했습니다.");
    /**
     * 204 no Content (2400 ~ 2499)
     */



    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public int getHttpStatusCode(){
        return httpStatus.value();
    }
}
