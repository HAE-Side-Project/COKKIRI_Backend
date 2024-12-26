package com.coggiri.main.domain.user.controller;

import com.coggiri.main.commons.Enums.ErrorType;
import com.coggiri.main.commons.Enums.SuccessType;
import com.coggiri.main.commons.response.CustomResponse;
import com.coggiri.main.domain.user.model.dto.request.UserLoginDTO;
import com.coggiri.main.domain.user.model.dto.request.UserCreateDTO;
import com.coggiri.main.domain.user.service.UserService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "사용자 로그인", description = "로그인 관련 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final UserService userService;

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
                                        "message": "로그인에 성공했습니다.",
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
                                    schema = @Schema(implementation = UserCreateDTO.class)
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2100",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 201,
                                        "code": 2100,
                                        "message": "회원가입에 성공했습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "5000",
                    description = "회원가입 실패",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 500,
                                        "code": 5000,
                                        "message": "알 수 없는 서버 에러가 발생했습니다"
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<CustomResponse<?>> register(@RequestBody UserCreateDTO userCreateDTO) {
        userService.create(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponse.success(SuccessType.SUCCESS_USER_CREATE));
    }

    @Operation(summary = "회원 탈퇴", description = "회원탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2002",
                    description = "회원탈퇴 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2002,
                                        "message": "회원탈퇴에 성공했습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4100",
                    description = "인증 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 401,
                                        "code": 4100,
                                        "message": "인증되지 않았습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "토큰이 존재하지 않습니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4009,
                                        "message": "토큰이 존재하지 않습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4401",
                    description = "존재하지 않는 유저",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 404,
                                        "code": 4401,
                                        "message": "존재하지 않는 유저입니다."
                                    }
                                    """
                            )
                    )
            ),
    })
    @DeleteMapping("delete")
    public ResponseEntity<CustomResponse<?>> deleteUser(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);

        if(token != null){
            try{
                userService.delete(token);
                return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_USER_DELETE));
            }catch (JwtException e){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomResponse.error(ErrorType.UNAUTHORIZED));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.error(ErrorType.INVALID_JWT_REQUEST));
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "id", schema = @Schema(type = "string", description = "사용자 아이디",example = "user123")),
                                            @SchemaProperty(name = "password", schema = @Schema(type = "string", description = "변경할 비밀번호",example = "12345"))
                                    }
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2002",
                    description = "비밀번호 변경 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2003,
                                        "message": "비밀번호 변경에 성공했습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4100",
                    description = "인증 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 401,
                                        "code": 4100,
                                        "message": "인증되지 않았습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "토큰이 존재하지 않습니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4009,
                                        "message": "토큰이 존재하지 않습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4401",
                    description = "존재하지 않는 유저",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 404,
                                        "code": 4401,
                                        "message": "존재하지 않는 유저입니다."
                                    }
                                    """
                            )
                    )
            ),
    })
    @ResponseBody
    @PutMapping("/password/change")
    public ResponseEntity<CustomResponse<?>> changePassword(@RequestBody UserLoginDTO userLoginDTO,HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);

        if(token != null){
            try{
                userService.changePassword(userLoginDTO,token);
                return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_USER_PASSWORD_CHANGE));
            }catch (JwtException e){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomResponse.error(ErrorType.UNAUTHORIZED));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.error(ErrorType.INVALID_JWT_REQUEST));
    }
}
