package com.coggiri.main.mvc.domain.entity;

import lombok.Getter;

@Getter
public class Member {
    private int userId;
    private int groupId;
    private int role;

    Member(int userId,int groupId,int role){
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
    }
}
