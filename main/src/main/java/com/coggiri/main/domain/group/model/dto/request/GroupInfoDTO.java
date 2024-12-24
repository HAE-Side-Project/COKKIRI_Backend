package com.coggiri.main.domain.group.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema
@Getter
public class GroupInfoDTO {
    @Schema(example = "1",description = "pk")
    private int groupId;
    @Schema(example = "0",defaultValue = "0",allowableValues = {"0","1"},description = "0: 스터디, 1: 프로젝트")
    private int groupCategory;
    @Schema(example = "TestStudy",description = "그룹 이름")
    private String groupName;
    @Schema(example = "10",description = "현재 인원수")
    private int groupNumber;
    @Schema(example = "asd.jpg",description = "이미지 파일 이름")
    private String thumbnailPath;
    @Schema(example = "소개글",description = "그룹 소개글")
    private String groupIntro;
    @Schema(example = "규칙",description = "그룹 규칙")
    private String groupRule;
    @Schema(example = "조건",description = "그룹 조건")
    private String groupCondition;
    @Setter
    @Schema(example = "project,study",description = "태그")
    private String[] tags;
    @Setter
    @Schema(example = "",description = "이미지 파일 데이터")
    private String thumbnailBase64;

    GroupInfoDTO(int groupId, int groupCategory, String groupName, int groupNumber,String thumbnailPath,
                 String groupIntro,String groupRule,String groupCondition){
        this.groupId = groupId;
        this.groupCategory = groupCategory;
        this.groupName = groupName;
        this.groupNumber = groupNumber;
        this.thumbnailPath = thumbnailPath;
        this.groupIntro = groupIntro;
        this.groupRule = groupRule;
        this.groupCondition = groupCondition;
    }
}
