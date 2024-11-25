package com.coggiri.main.mvc.domain.dto;

import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserDTO {
    private int id;
    private String userId;
    private String password;
    private String userName;
    private String email;
    private List<UserGroupRole> roles = new ArrayList<>();

    @JsonCreator
    public UserDTO(
            @JsonProperty("userId") String userId,
            @JsonProperty("password") String password,
            @JsonProperty("userName") String userName,
            @JsonProperty("email") String email) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }

    public UserDTO(String userId, String password, String userName, String email,List<UserGroupRole>roles) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.roles = roles;
    }

    public UserDTO(int id, String userId, String password, String userName, String email,List<UserGroupRole>roles) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.roles = roles;
    }

    public User toUser(String encodedPassword,List<UserGroupRole> roles){
        return new User(id,this.userId,encodedPassword,this.userName,this.email,roles);
    }

}
