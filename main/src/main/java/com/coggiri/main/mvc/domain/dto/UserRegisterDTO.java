package com.coggiri.main.mvc.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserRegisterDTO {
    private String userId;
    private String password;
    private String userName;
    private String email;

    @JsonCreator
    public UserRegisterDTO(
            @JsonProperty("userId") String userId,
            @JsonProperty("password") String password,
            @JsonProperty("userName") String userName,
            @JsonProperty("email") String email) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }
}
