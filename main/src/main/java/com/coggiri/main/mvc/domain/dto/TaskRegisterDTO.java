package com.coggiri.main.mvc.domain.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskRegisterDTO {
    private int userId;
    private int groupId;
    private boolean groupOption;
    private String taskName;
    private int taskType;
    private LocalDate registerDate;
    private LocalDate dueDate;

    TaskRegisterDTO(int userId, int groupId, boolean groupOption,
                    String taskName, int taskType, LocalDate registerDate,
                    LocalDate dueDate){
        this.userId = userId;
        this.groupId = groupId;
        this.groupOption = groupOption;
        this.taskName = taskName;
        this.taskType = taskType;
        this.registerDate = registerDate;
        this.dueDate = dueDate;
    }
}
