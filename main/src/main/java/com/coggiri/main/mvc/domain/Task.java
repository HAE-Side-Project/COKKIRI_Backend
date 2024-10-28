package com.coggiri.main.mvc.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Task {
    private int taskId;
    private int id;
    private boolean groupOption;
    private String taskName;
    private int taskType;
    private LocalDateTime registerDate;
    private LocalDateTime dueDate;

    Task(int taskId, int id, boolean groupOption, String taskName, int taskType, LocalDateTime registerDate, LocalDateTime dueDate){
        this.taskId = taskId;
        this.id = id;
        this.groupOption = groupOption;
        this.taskName = taskName;
        this.taskType = taskType;
        this.registerDate = registerDate;
        this.dueDate = dueDate;
    }
}