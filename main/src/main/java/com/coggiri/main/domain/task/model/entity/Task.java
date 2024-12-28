package com.coggiri.main.domain.task.model.entity;

import com.coggiri.main.domain.task.model.dto.request.TaskRegisterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Schema
@Getter
public class Task {
    @Schema(example = "1",description = "태스크 pk")
    private Long taskId;
    @Schema (example = "true",description = "개인 태스크:false, 그룹 태스크:true")
    private boolean groupOption;
    @Schema(example = "true",description = "완료 여부")
    private boolean achieveCondition;
    @Schema (example = "밥먹기",description = "태스크 정의")
    private String taskName;
    @Schema (example = "0", description = "0: 투두 , 1: 태스크")
    private int taskType;
    @Schema (example = "2024-12-02", description = "등록날짜")
    private LocalDate registerDate;
    @Schema (example = "2024-12-02", description = "마감날짜")
    private LocalDate dueDate;

    Task(Long taskId, boolean groupOption, boolean achieveCondition,
         String taskName, int taskType, LocalDate registerDate, LocalDate dueDate){
        this.taskId = taskId;
        this.groupOption = groupOption;
        this.achieveCondition = achieveCondition;
        this.taskName = taskName;
        this.taskType = taskType;
        this.registerDate = registerDate;
        this.dueDate = dueDate;
    }

    public Task(TaskRegisterDTO taskRegisterDTO){
        this.groupOption = taskRegisterDTO.isGroupOption();
        this.achieveCondition = false;
        this.taskName = taskRegisterDTO.getTaskName();
        this.taskType = taskRegisterDTO.getTaskType();
        this.registerDate = taskRegisterDTO.getRegisterDate();
        this.dueDate = taskRegisterDTO.getDueDate();
    }
}
