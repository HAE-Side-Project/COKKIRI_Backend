package com.coggiri.main.domain.task.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Schema
@Getter
public class TaskRegisterDTO {
    @Schema (example = "true",description = "개인 태스크:false, 그룹 태스크:true")
    private boolean groupOption;
    @Schema (example = "밥먹기",description = "태스크 정의")
    private String taskName;
    @Schema (example = "0", description = "0: 투두 , 1: 태스크")
    private int taskType;
    @Schema (example = "2024-12-02", description = "등록날짜")
    private LocalDate registerDate;
    @Schema (example = "2024-12-02", description = "마감날짜")
    private LocalDate dueDate;
    @Schema (example = "0",description = "그룹 pk")
    private int groupId;
    @Schema (example = "[1,2,3]",description = "관련 멤버")
    private int[] userId;
    @Schema (example = "[\"backend\",\"frontend\"]",description = "태그")
    private String[] tags;

    public TaskRegisterDTO(){

    }

    TaskRegisterDTO(boolean groupOption,
                    String taskName, int taskType, LocalDate registerDate,
                    LocalDate dueDate,int groupId,int[] userId, String[] tags){
        this.groupOption = groupOption;
        this.taskName = taskName;
        this.taskType = taskType;
        this.registerDate = registerDate;
        this.dueDate = dueDate;
        this.groupId = groupId;
        this.userId = userId;
        this.tags = tags;
    }
}
