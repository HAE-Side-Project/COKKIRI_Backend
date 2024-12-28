package com.coggiri.main.domain.task.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class TaskInfoDTO {
    private Long taskId;
    private Long groupId;
    private Long[] userId;
    @Setter
    private String[] tags;

    TaskInfoDTO(){

    }

    public TaskInfoDTO(Long taskId, Long groupId, Long[] userId){
        this.taskId = taskId;
        this.groupId = groupId;
        this.userId = userId;
    }
}
