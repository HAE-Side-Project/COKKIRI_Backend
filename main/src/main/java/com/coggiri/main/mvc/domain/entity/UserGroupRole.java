package com.coggiri.main.mvc.domain.entity;

import com.coggiri.main.customEnums.Role;
import lombok.Getter;

@Getter
public class UserGroupRole {
    private int id;
    private int userId;
    private int groupId;
    private String role;

    public UserGroupRole(int userId, int groupId, String role){
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
    }
}
