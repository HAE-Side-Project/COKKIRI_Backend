package com.coggiri.main.commons.Enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {
    /*
     * 400 Bad Request (4000 ~ 4099)
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST,4000,"잘못된 요청입니다."),
    REQUEST_VALIDATION(HttpStatus.BAD_REQUEST,4001,"잘못된 요청입니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST,4002,"잘못된 타입 요청입니다."),
    INVALID_MISSING_HEADER(HttpStatus.BAD_REQUEST, 4003, "요청에 필요한 헤더값이 존재하지 않습니다."),
    INVALID_HTTP_REQUEST(HttpStatus.BAD_REQUEST, 4004, "허용되지 않는 문자열이 입력되었습니다."),
    INVALID_BOARD_DELETE_REQUEST(HttpStatus.BAD_REQUEST, 4005, "존재하지 않는 게시글 삭제 요청 입니다."),
    INVALID_BOARD_CREATE_REQUEST(HttpStatus.BAD_REQUEST, 4006, "게시글 생성에 실패했습니다."),
    INVALID_USER_LOGIN_REQUEST(HttpStatus.BAD_REQUEST,4007,"존재하지 않는 유저의 로그인 요청입니다."),
    INVALID_USER_CREATE_ID(HttpStatus.BAD_REQUEST,4008,"이미 존재하는 사용자 아이디입니다."),
    INVALID_JWT_REQUEST(HttpStatus.BAD_REQUEST,4009,"토큰이 존재하지 않습니다"),
    INVALID_EMAIL_USED(HttpStatus.BAD_REQUEST,4010,"이미 인증에 사용된 이메일입니다."),
    INVALID_EMAIL_NOT_USED(HttpStatus.BAD_REQUEST,4011,"인증번호를 발급받지 않은 이메일입니다."),
    INVALID_EMAIL_ACCESS_FREQUENT(HttpStatus.BAD_REQUEST,4012,"비정상적으로 잦은 접근 시도입니다."),
    INVALID_EMAIL_AUTH_CODE_DIFFERENT(HttpStatus.BAD_REQUEST,4013,"발급된 인증번호와 다릅니다."),
    INVALID_ID_DUPLICATE(HttpStatus.BAD_REQUEST,4014,"이미 존재하는 아이디입니다."),
    INVALID_FILE_NOT_EXTENSION(HttpStatus.BAD_REQUEST,4015,"허용되지 않은 확장자입니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST,4016,"잘못된 페이지 숫자입니다."),
    INVALID_GROUP_ID(HttpStatus.BAD_REQUEST,4017,"그룹 ID 인자가 없습니다."),
    /*
    *  401 Unauthorized (4100 ~ 4199)
    */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 4100, "인증되지 않았습니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, 4101, "알 수 없는 이유로 요청이 거절되었습니다."),
    UNAUTHORIZED_EMAIL_EXPIRED(HttpStatus.UNAUTHORIZED,4102,"만료된 인증번호입니다."),
    UNAUTHORIZED_USER_GROUP(HttpStatus.UNAUTHORIZED,4103,"그룹에 대한 권한이 없습니다."),
    /*
     * 403 Forbidden (4300 ~ 4399)
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, 4300, "해당 자원에 접근 권한이 없습니다."),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, 4301, "사용자 접근 권한이 없습니다"),
    FORBIDDEN_USER_MODIFY(HttpStatus.FORBIDDEN, 4302, "사용자 수정 권한이 없습니다"),
    /*
     * 404 Not Found (4400 ~ 4499)
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, 4400, "존재하지 않는 리소스입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 4401, "존재하지 않는 유저입니다."),
    NOT_FOUND_USER_GROUP(HttpStatus.NOT_FOUND,4402,"그룹에 대한 사용자 정보가 없습니다"),
    NOT_FOUND_THUMBNAIL(HttpStatus.NOT_FOUND,4403,"썸네일이 존재하지 않습니다."),
    /*
     * 405 Method Not Allowed (4500 ~ 4599)
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,4500,"잘못된 HTTP method 요청입니다."),
    /*
     * 500 Internal_SERVER (5000 ~ 5099)
     */
    INTERNAL_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "알 수 없는 서버 에러가 발생했습니다"),
    INTERNAL_MAIL_SEND(HttpStatus.INTERNAL_SERVER_ERROR, 5001,"인증 메일을 전송 에러가 발생했습니다."),
    INTERNAL_GROUP_CREATE(HttpStatus.INTERNAL_SERVER_ERROR,5002,"그룹 생성 에러가 발생했습니다."),
    INTERNAL_THUMBNAIL_DELETE(HttpStatus.INTERNAL_SERVER_ERROR,5003,"썸네일 파일 삭제 실패했습니다."),
    INTERNAL_TAG_DELETE(HttpStatus.INTERNAL_SERVER_ERROR,5004,"태그 삭제에 실패했습니다."),
    INTERNAL_TAG_CREATE(HttpStatus.INTERNAL_SERVER_ERROR,5005,"태그 추가에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public int getHttpStatusCode(){
        return httpStatus.value();
    }
}
