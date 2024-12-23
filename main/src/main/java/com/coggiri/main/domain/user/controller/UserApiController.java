package com.coggiri.main.domain.user.controller;

import com.coggiri.main.domain.user.model.dto.UserLoginDTO;
import com.coggiri.main.domain.user.model.dto.UserDTO;
import com.coggiri.main.domain.user.model.entity.JwtToken;
import com.coggiri.main.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "사용자 로그인", description = "로그인 관련 API")
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private UserService userService;

    @Autowired
    UserApiController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "로그인", description = "로그인 기능 API",
            responses= {
                @ApiResponse(
                        responseCode = "200",
                        description = "로그인 및 JWT 토큰 반환",
                        content = @Content(
                                schemaProperties = {
                                        @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                        @SchemaProperty(name = "JwtToken", schema = @Schema(implementation = JwtToken.class,description = "JWT 토큰")),
                                        @SchemaProperty(name = "userId", schema = @Schema(type = "string",description = "유저 아이디" ,example = "user"))
                                }
                        )
                )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schemaProperties = {
                                        @SchemaProperty(name = "userId", schema = @Schema(type = "string", description = "아이디",example = "user")),
                                        @SchemaProperty(name = "password", schema = @Schema(type = "string", description = "비밀번호",example = "1234"))
                                }
                        )
                    }
            )
    )
    @PostMapping("login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody UserLoginDTO userLoginDTO){
        Map<String, Object> response = new HashMap<>();
        try {
            JwtToken jwtToken = userService.login(userLoginDTO.getUserId(), userLoginDTO.getPassword());
            log.debug("request username = {}, password = {}", userLoginDTO.getUserId(), userLoginDTO.getPassword());
            log.debug("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
            response.put("success",true);
            response.put("JwtToken",jwtToken);
            response.put("userId", userLoginDTO.getUserId());
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원가입",description = "회원가입 기능 API",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "회원가입 성공 여부 반환",
                        content = @Content(
                                schemaProperties = {
                                        @SchemaProperty(name = "success",schema = @Schema(type = "boolean",description = "성공 여부")),
                                        @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "오류 or 성공 메세지"))
                                }
                        )
                )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "userId", schema = @Schema(type = "string", description = "아이디",example = "abcd1234")),
                                            @SchemaProperty(name = "password", schema = @Schema(type = "string", description = "비밀번호",example = "1234")),
                                            @SchemaProperty(name = "userName", schema = @Schema(type = "string", description = "이름",example = "홍길동")),
                                            @SchemaProperty(name = "email", schema = @Schema(type = "string", description = "이메일",example = "example@naver.com")),
                                    }
                            )
                    }
            )
    )
    @PostMapping("register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDTO userInfo) {
        Map<String, Object> response = new HashMap<>();

        try {
            userService.register(userInfo);
            response.put("success", true);
            response.put("message","회원가입 완료");
        } catch (Exception e) {
            response.put("success",false);
            response.put("message", (e.getMessage() != null) ? e.getMessage() : "Unknown error occurred");
        }

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "회원 탈퇴", description = "회원탈퇴 API",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "회원 탈퇴 완료",
                        content = @Content(
                                schemaProperties = {
                                        @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                        @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "메세지",example = "회원 탈퇴 완료" ))
                                }
                        )
                )},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = {
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schemaProperties = {
                                        @SchemaProperty(name = "userId", schema = @Schema(type = "string", description = "user pk",example = "1")),
                                }
                        )
                }
        )
    )
    @PostMapping("deleteUser")
    public ResponseEntity<Map<String,Object>> deleteUser(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        int userId = Integer.parseInt(map.get("userId").toString());
        try{
            userService.deleteUser(userId);
            response.put("success",true);
            response.put("message","회원 탈퇴 완료");
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 API",
        responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 변경",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                    @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "메세지",example = "비밀번호 변경 완료" ))
                            }
                    )
            )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "id", schema = @Schema(type = "string", description = "그룹 pk or 태스크 pk",example = "1")),
                                            @SchemaProperty(name = "type", schema = @Schema(type = "string", description = "그룹/태스크 태그 구별",example = "GROUP/TASK"))
                                    }
                            )
                    }
            )
    )
    @ResponseBody
    @PostMapping("changePassword")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody UserLoginDTO userLoginDTO){
        Map<String, Object> response = new HashMap<>();

        try{
            userService.changePassword(userLoginDTO);
            response.put("success",true);
            response.put("message","비밀변경 완료");
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
