package com.coggiri.main.mvc.domain.entity;

import com.coggiri.main.mvc.domain.dto.TaskRegisterDTO;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Task {
    private int taskId;
    private int userId;
    private int groupId;
    private boolean groupOption; //개인 or 그룹 태스크
    private boolean achiveCondition;
    private String taskName;
    private int taskType;
    private LocalDate registerDate;
    private LocalDate dueDate;

    Task(int taskId, int userId, int groupId, boolean groupOption,
         boolean achiveCondition, String taskName, int taskType,
         LocalDate registerDate, LocalDate dueDate){

        this.taskId = taskId;
        this.userId = userId;
        this.groupId = groupId;
        this.groupOption = groupOption;
        this.achiveCondition = achiveCondition;
        this.taskName = taskName;
        this.taskType = taskType;
        this.registerDate = registerDate;
        this.dueDate = dueDate;
    }

    Task(TaskRegisterDTO taskRegisterDTO){
        this.userId = taskRegisterDTO.getUserId();
        this.groupId = taskRegisterDTO.getGroupId();
        this.groupOption = taskRegisterDTO.isGroupOption();
        this.achiveCondition = false;
        this.taskName = taskRegisterDTO.getTaskName();
        this.taskType = taskRegisterDTO.getTaskType();
        this.registerDate = taskRegisterDTO.getRegisterDate();
        this.dueDate = taskRegisterDTO.getDueDate();
    }
}
