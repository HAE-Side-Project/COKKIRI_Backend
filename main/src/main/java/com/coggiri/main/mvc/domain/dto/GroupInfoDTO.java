package com.coggiri.main.mvc.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

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

    GroupInfoDTO(int groupId, int groupCategory, String groupName, int groupNumber,String thumbnailPath){
        this.groupId = groupId;
        this.groupCategory = groupCategory;
        this.groupName = groupName;
        this.groupNumber = groupNumber;
        this.thumbnailPath = thumbnailPath;
    }
}
