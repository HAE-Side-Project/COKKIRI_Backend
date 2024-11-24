package com.coggiri.main.mvc.controller;

import com.coggiri.main.jwtUtils.RequireGroupRole;
import com.coggiri.main.mvc.domain.dto.UserLoginDTO;
import com.coggiri.main.mvc.domain.dto.UserDTO;
import com.coggiri.main.mvc.domain.entity.JwtToken;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "사용자 로그인", description = "로그인 관련 API")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private UserService userService;

    @Autowired
    UserController(UserService userService){
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
                                        @SchemaProperty(name = "userId", schema = @Schema(type = "string",description = "유저 아이디"))
                                }
                        )
                )

    })
    @PostMapping("login")
    public ResponseEntity<Map<String,Object>> login(@Parameter(description = "사용자 정보") @RequestBody UserLoginDTO userLoginDTO){
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
                )
            }
    )
    @PostMapping("register")
    public ResponseEntity<Map<String, Object>> register(@Parameter(description = "사용자 정보") @RequestBody UserDTO userInfo) {
        Map<String, Object> response = new HashMap<>();

        log.debug("register called");

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

    @ResponseBody
    @PostMapping("changePassword")
    public ResponseEntity<Map<String, Object>> changePassword(@Parameter(description = "사용자 정보", example = "userId") @RequestBody UserLoginDTO userLoginDTO){
        Map<String, Object> response = new HashMap<>();
        Optional<User> user = userService.findUserById(userLoginDTO.getUserId());

        try{
            userService.changePassword(user,userLoginDTO.getPassword());
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
