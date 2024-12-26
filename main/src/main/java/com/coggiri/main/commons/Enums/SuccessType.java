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
    SUCCESS_USER_PASSWORD_CHANGE(HttpStatus.OK,2003,"비밀번호 변경에 성공했습니다"),
    SUCCESS_EMAIL_SEND(HttpStatus.OK,2004,"인증번호 발급에 성공했습니다."),
    SUCCESS_EMAIL_VERIFY(HttpStatus.OK,2005,"인증 성공했습니다."),
    SUCCESS_VALIDATE_ID(HttpStatus.OK,2006,"아이디 중복검사 성공했습니다"),
    /*
    *  201 create (2100 ~ 2199)
    * */
    SUCCESS_USER_CREATE(HttpStatus.CREATED,2100,"회원가입에 성공했습니다."),
    SUCCESS_GROUP_CREATE(HttpStatus.CREATED,2101,"그룹 생성에 성공했습니다.");
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
