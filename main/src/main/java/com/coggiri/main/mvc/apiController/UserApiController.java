package com.coggiri.main.mvc.apiController;

import com.coggiri.main.mvc.domain.dto.UserLoginDTO;
import com.coggiri.main.mvc.domain.dto.UserDTO;
import com.coggiri.main.mvc.domain.entity.JwtToken;
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
import org.springframework.security.core.parameters.P;
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
                                        @SchemaProperty(name = "userId", schema = @Schema(type = "string",description = "유저 아이디"))
                                }
                        )
                )},
            parameters = {
                    @Parameter(name = "userId", description = "아이디", example = "abc1234"),
                    @Parameter(name = "password",description = "비밀번호",example = "1234")
            }
    )
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

    @Operation(summary = "회원가입",description = "회원가입 기능 API", tags = {"user"})
    @ApiResponses(value = { @ApiResponse(description = "success",content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)))})
    @PostMapping(value = "register", consumes = {"application/json"})
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

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 API")
    @ApiResponse(
            responseCode = "200",
            description = "비밀번호 변경",
            content = @Content(
                    schemaProperties = {
                            @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                            @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "메세지"))
                    }
            )
    )
    @Parameters(
            value = {
                    @Parameter(name = "userId", description = "아이디", example = "abcd1234"),
                    @Parameter(name = "password", description = "변경할 비밀번호", example = "1234")
            }
    )
    @ResponseBody
    @PostMapping("changePassword")
    public ResponseEntity<Map<String, Object>> changePassword(@Parameter(description = "사용자 정보", example = "userId") @RequestBody UserLoginDTO userLoginDTO){
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
