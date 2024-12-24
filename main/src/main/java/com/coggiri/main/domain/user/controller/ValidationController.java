package com.coggiri.main.domain.user.controller;

import com.coggiri.main.commons.Enums.EmailErrorStatus;
import com.coggiri.main.domain.user.model.dto.request.MailDTO;
import com.coggiri.main.domain.user.model.dto.request.VerificationInfo;
import com.coggiri.main.domain.user.service.MailService;

import com.coggiri.main.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequestMapping("/api/validate")
public class ValidationController {
    private final MailService mailService;
    private final UserService userService;

    @Autowired
    ValidationController(MailService mailService, UserService userService){
        this.mailService = mailService;
        this.userService = userService;
    }

    @Operation(summary = "이메일 인증번호 발급", description = "이메일 인증 API",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "인증 코드 전송 완료 및 코드 정보 반환",
                        content = @Content(
                                schemaProperties = {
                                        @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                        @SchemaProperty(name = "authCode",schema = @Schema(type = "string",description = "인증 코드"))
                                }
                        )
                )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "email", schema = @Schema(type = "string", description = "이메일",example = "sample@naver.com")),
                                            @SchemaProperty(name = "authCode", schema = @Schema(type = "string", description = "인증번호",example = "124asd234"))
                                    }
                            )
                    }
            )
    )
    @ResponseBody
    @PostMapping("/sendEmail")
    public ResponseEntity<Map<String,Object>> sendVerificationEmail(@RequestBody MailDTO mailDTO) throws MessagingException, UnsupportedEncodingException{
        Map<String, Object> response = new HashMap<>();
        try{
            if(userService.findUserByEmail(mailDTO.getEmail()).isPresent()){
                throw new IllegalArgumentException("이미 인증에 사용된 이메일입니다.");
            }
            VerificationInfo ret = mailService.sendSimpleMessage(mailDTO.getEmail());
            response.put("success",true);
            response.put("authCode",ret.getCode());
            response.put("expireTime",ret.getExpireDate());
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "인증번호 검증",description = "이메일 인증번호 검증 API",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "이메일 인증번호 검증",
                        content = @Content(
                                schemaProperties = {
                                        @SchemaProperty(name = "success", schema = @Schema(type = "boolean", description = "성공 여부")),
                                        @SchemaProperty(name = "message", schema = @Schema(type = "string", description = "성공 및 에러 메세지",example = "인증이 완료되었습니다"))
                                }
                        )
                )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "email", schema = @Schema(type = "string", description = "이메일",example = "sample@naver.com")),
                                            @SchemaProperty(name = "authCode", schema = @Schema(type = "string", description = "인증번호",example = "124asd234"))
                                    }
                            )
                    }
            )
    )
    @ResponseBody
    @PostMapping("/verifyEmail")
    public ResponseEntity<Map<String,Object>> validateCode(@Parameter(description = "이메일 정보") @RequestBody MailDTO mailDTO){
        Map<String,Object> response = new HashMap<>();
        EmailErrorStatus result = mailService.verifyCode(mailDTO.getEmail(),mailDTO.getAuthCode());

        switch (result){
            case NOT_EXIST:
                response.put("success",false);
                response.put("message","인증번호를 발급받지 않은 이메일입니다.");
                break;
            case EXPIRED:
                response.put("success",false);
                response.put("message","만료된 인증번호입니다.");
                break;

            case ILLEGAL_ACCESS:
                response.put("success",false);
                response.put("message","비정상적으로 잦은 접근 시도입니다.");
                break;
            case NOT_EQUAL:
                response.put("success",false);
                response.put("message","발급된 인증번호와 다릅니다");
                break;
            case VALID:
                response.put("success",true);
                response.put("message","인증이 완료되었습니다");
                break;
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "아이디 유효 검사", description = "아이디 중복 검사 API",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "아이디 유효성 검사 및 아이디 반환",
                        content = @Content(
                                schemaProperties = {
                                        @SchemaProperty(name = "success", schema = @Schema(type = "boolean", description = "결과 여부")),
                                        @SchemaProperty(name = "userId", schema = @Schema(type = "string", description = "유저 아이디"))
                                }
                        )
                )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "userId", schema = @Schema(type = "string", description = "아이디",example = "user"))
                                    }
                            )
                    }
            )
    )
    @PostMapping("/Id")
    public ResponseEntity<Map<String,Object>> validateId(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        String userId = map.get("userId").toString();
        if(userService.findUserById(userId).isEmpty()){
            response.put("success",true);
            response.put("userId",userId);
        }else{
            response.put("success",false);
        }

        return ResponseEntity.ok(response);
    }
}
