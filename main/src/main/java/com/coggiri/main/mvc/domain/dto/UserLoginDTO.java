package com.coggiri.main.mvc.domain.dto;

import com.coggiri.main.mvc.domain.entity.User;
import lombok.Getter;

@Getter
public class UserLoginDTO {
    private String userId;
    private String password;

    public UserLoginDTO(){}

    public UserLoginDTO(String userId, String password){
        this.userId = userId;
        this.password = password;
    }

}
