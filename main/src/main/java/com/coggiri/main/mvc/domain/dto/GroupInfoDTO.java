package com.coggiri.main.mvc.domain.dto;

import lombok.Getter;

@Getter
public class GroupInfoDTO {
    private int groupId;
    private int groupCategory;
    private String groupName;
    private int groupNumber;
    private String thumbnailPath;

    GroupInfoDTO(int groupId, int groupCategory, String groupName, int groupNumber,String thumbnailPath){
        this.groupId = groupId;
        this.groupCategory = groupCategory;
        this.groupName = groupName;
        this.groupNumber = groupNumber;
        this.thumbnailPath = thumbnailPath;
    }
}
