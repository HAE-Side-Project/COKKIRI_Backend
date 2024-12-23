package com.coggiri.main.domain.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema
@Getter
public class UserLoginDTO {
    @Schema(example = "asdas1234",description = "아이디")
    private String userId;
    @Schema(example = "1234",description = "비밀번호")
    private String password;

    public UserLoginDTO(){}

    public UserLoginDTO(String userId, String password){
        this.userId = userId;
        this.password = password;
    }

}
