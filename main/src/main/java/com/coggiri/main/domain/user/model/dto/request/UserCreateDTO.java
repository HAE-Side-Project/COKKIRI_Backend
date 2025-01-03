package com.coggiri.main.domain.user.model.dto.request;

import com.coggiri.main.domain.user.model.entity.User;
import com.coggiri.main.domain.user.model.entity.UserGroupRole;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Schema
@Getter
public class UserCreateDTO {
    @Schema(example = "asd1234", description = "아이디")
    private String userId;
    @Schema(example = "1234", description = "비밀번호")
    private String password;
    @Schema(example = "user", description = "사용자 이름")
    private String userName;
    @Schema(example = "sample@naver.com", description = "이메일")
    private String email;

    @JsonCreator
    public UserCreateDTO(
            @JsonProperty("userId") String userId,
            @JsonProperty("password") String password,
            @JsonProperty("userName") String userName,
            @JsonProperty("email") String email) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }

    public UserCreateDTO(String userId, String password, String userName, String email, List<UserGroupRole>roles) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }

    public UserCreateDTO(int id, String userId, String password, String userName, String email, List<UserGroupRole>roles) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }
}
