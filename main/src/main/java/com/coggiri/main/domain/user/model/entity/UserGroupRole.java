package com.coggiri.main.domain.user.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema
@Getter
public class UserGroupRole {
    @Schema(example = "1",description = "pk")
    private int id;
    @Schema(example = "1",description = "user pk")
    private int userId;
    @Schema(example = "1",description = "group pk")
    private int groupId;
    @Schema(example = "USER",description = "권한")
    private String role;

    UserGroupRole(){

    }

    public UserGroupRole(int userId, int groupId, String role){
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
    }
}