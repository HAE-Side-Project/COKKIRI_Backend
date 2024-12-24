package com.coggiri.main.domain.user.controller;

import com.coggiri.main.commons.Enums.SuccessType;
import com.coggiri.main.commons.response.CustomResponse;
import com.coggiri.main.domain.user.model.dto.request.UserLoginDTO;
import com.coggiri.main.domain.user.model.dto.request.UserDTO;
import com.coggiri.main.domain.user.model.entity.JwtToken;
import com.coggiri.main.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Operation(
            summary = "로그인",
            description = "로그인 기능 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserLoginDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2001",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2001,
                                        "message": "로그인 성공",
                                        "data": {
                                            "jwtToken": "eyJhbGciOiJIUzI1NiJ9...",
                                            "userId": "user123"
                                        }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4007",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4007,
                                        "message": "잘못된 요청입니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4100",
                    description = "존재하지 않는 유저",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4100,
                                        "message": "존재하지 않는 유저입니다"
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<CustomResponse<?>> login(@RequestBody UserLoginDTO userLoginDTO){
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_USER_LOGIN,userService.login(userLoginDTO.getUserId(),userLoginDTO.getPassword())));
    }

    @Operation(summary = "회원가입",description = "회원가입 기능 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2001,
                                        "message": "로그인 성공",
                                        "data": {
                                            "jwtToken": "eyJhbGciOiJIUzI1NiJ9...",
                                            "userId": "user123"
                                        }
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/create")
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
