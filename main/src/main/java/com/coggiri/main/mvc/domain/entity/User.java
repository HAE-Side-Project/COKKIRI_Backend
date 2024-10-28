package com.coggiri.main.mvc.domain.entity;

import lombok.Getter;

@Getter
public class User {
    private int id;
    private String userId;
    private String password;
    private String userName;
    private String email;

    User(int id,String userId,String password,String userName,String email){
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }
}
