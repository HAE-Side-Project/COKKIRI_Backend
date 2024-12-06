package com.coggiri.main.mvc.domain.dto;

import lombok.Getter;

@Getter
public class GroupTagDTO {
    int groupId;
    int[] tagId;

    GroupTagDTO(){

    }

    GroupTagDTO(int groupId,int[] tagId){
        this.groupId = groupId;
        this.tagId = tagId;
    }
}
