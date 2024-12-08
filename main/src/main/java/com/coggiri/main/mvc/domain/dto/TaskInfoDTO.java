package com.coggiri.main.mvc.domain.dto;

import lombok.Getter;

@Getter
public class TaskRoleInfoDTO {
    private int taskId;
    private int groupId;
    private int[] userId;

    public TaskRoleInfoDTO(int taskId,int groupId, int[] userId){
        this.taskId = taskId;
        this.groupId = groupId;
        this.userId = userId;
    }
}
