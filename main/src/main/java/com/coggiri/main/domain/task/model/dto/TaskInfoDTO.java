package com.coggiri.main.domain.task.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class TaskInfoDTO {
    private int taskId;
    private int groupId;
    private int[] userId;
    @Setter
    private String[] tags;

    TaskInfoDTO(){

    }

    public TaskInfoDTO(int taskId, int groupId, int[] userId){
        this.taskId = taskId;
        this.groupId = groupId;
        this.userId = userId;
    }
}
