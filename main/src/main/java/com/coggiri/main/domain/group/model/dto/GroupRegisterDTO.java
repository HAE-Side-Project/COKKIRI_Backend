package com.coggiri.main.domain.group.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema
@Getter
public class GroupRegisterDTO {
    @Schema(description = "그룹 종류",defaultValue = "0",allowableValues = {"0","1"},example = "0")
    private int groupCategory;
    @Schema(description = "그룹 이름",example = "그룹 이름")
    private String groupName;
    @Schema(description = "그룹 소개",example = "그룹 소개")
    private String groupIntro;
    @Schema(description = "그룹 규칙",example = "그룹 규칙")
    private String groupRule;
    @Schema(description = "그룹 조건",example = "그룹 조건")
    private String groupCondition;
    @Schema(description = "그룹 태그",example = "그룹 태그")
    private String[] groupTags;

    GroupRegisterDTO() {

    }

    GroupRegisterDTO(int groupCategory,String groupName, String groupIntro,
                     String groupRule, String groupCondition,String[] tags){
        this.groupCategory = groupCategory;
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.groupRule = groupRule;
        this.groupCondition = groupCondition;
        this.groupTags = tags;
    }
}
