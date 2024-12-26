package com.coggiri.main.domain.user.controller;

import com.coggiri.main.commons.Enums.EmailErrorStatus;
import com.coggiri.main.commons.Enums.ErrorType;
import com.coggiri.main.commons.Enums.SuccessType;
import com.coggiri.main.commons.exception.customException;
import com.coggiri.main.commons.response.CustomResponse;
import com.coggiri.main.domain.user.model.dto.request.MailDTO;
import com.coggiri.main.domain.user.model.dto.request.VerificationInfo;
import com.coggiri.main.domain.user.service.MailService;

import com.coggiri.main.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "email", schema = @Schema(type = "string", description = "이메일",example = "sample@naver.com"))
                                    }
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2004",
                    description = "인증메일 발급 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2004,
                                        "message": "인증번호 발급에 성공했습니다.",
                                        "data": {
                                            "code": "8f0cCpwi",
                                            "expireDate": "2024-12-26T16:29:11.2368402",
                                            "attempts": 0
                                        }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "5001",
                    description = "메일 전송 실패",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 500,
                                        "code": 5001,
                                        "message": "인증 메일을 전송하는데 에러가 발생했습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @ResponseBody
    @GetMapping("/email/send")
    public ResponseEntity<CustomResponse<?>> sendVerificationEmail(@RequestBody MailDTO mailDTO) throws MessagingException, UnsupportedEncodingException{

        if(userService.findUserByEmail(mailDTO.getEmail()).isPresent()){
            throw new customException(ErrorType.INVALID_EMAIL_USED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_EMAIL_SEND,mailService.sendSimpleMessage(mailDTO.getEmail())));
    }

    @Operation(summary = "인증번호 검증",description = "이메일 인증번호 검증 API",
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2005",
                    description = "인증 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2005,
                                        "message": "인증 성공했습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4010",
                    description = "이미 인증에 사용된 이메일입니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4010,
                                        "message": "이미 인증에 사용된 이메일입니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4011",
                    description = "인증번호를 발급받지 않은 이메일입니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4011,
                                        "message": "인증번호를 발급받지 않은 이메일입니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4012",
                    description = "비정상적으로 잦은 접근 시도입니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4012,
                                        "message": "비정상적으로 잦은 접근 시도입니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4013",
                    description = "발급된 인증번호와 다릅니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4013,
                                        "message": "발급된 인증번호와 다릅니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4102",
                    description = "만료된 인증번호입니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 401,
                                        "code": 4102,
                                        "message": "만료된 인증번호입니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @ResponseBody
    @GetMapping("/email/verify")
    public ResponseEntity<CustomResponse<?>> validateCode(@Parameter(description = "이메일 정보") @RequestBody MailDTO mailDTO){
        mailService.verifyCode(mailDTO.getEmail(),mailDTO.getAuthCode());
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_EMAIL_VERIFY));
    }

    @Operation(summary = "아이디 유효 검사", description = "아이디 중복 검사 API",
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2006",
                    description = "아이디 중복검사 성공했습니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2006,
                                        "message": "아이디 중복검사 성공했습니다"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4014",
                    description = "메일 전송 실패",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4014,
                                        "message": "이미 존재하는 아이디입니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/id")
    public ResponseEntity<CustomResponse<?>> validateId(@RequestBody Map<String,Object> map){
        String userId = map.get("userId").toString();
        if(userService.findUserByUserName(userId).isPresent()) throw new customException(ErrorType.INVALID_ID_DUPLICATE);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_VALIDATE_ID));
    }
}
