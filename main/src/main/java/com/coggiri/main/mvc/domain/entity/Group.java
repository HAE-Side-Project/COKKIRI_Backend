package com.coggiri.main.mvc.domain.entity;

import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import lombok.Getter;

@Getter
public class Group {
    private int groupId;
    private int groupCategory;
    private String groupName;
    private String groupIntro;
    private String groupRule;
    private String groupCondition;
    private int groupNumber = 1;
    private String thumbnailPath;


    Group(int groupId, int groupCategory, String groupName, String groupIntro,
          String groupRule, String groupCondition,int groupNumber ,String thumbnailPath){
        this.groupId = groupId;
        this.groupCategory = groupCategory;
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.groupRule = groupRule;
        this.groupCondition =groupCondition;
        this.groupNumber = groupNumber;
        this.thumbnailPath = thumbnailPath;
    }

    public Group(GroupRegisterDTO groupRegisterDTO,String thumbnailPath){
        this.groupCategory = groupRegisterDTO.getGroupCategory();
        this.groupName = groupRegisterDTO.getGroupName();
        this.groupIntro = groupRegisterDTO.getGroupIntro();
        this.groupRule = groupRegisterDTO.getGroupRule();
        this.groupCondition = groupRegisterDTO.getGroupCondition();
        this.thumbnailPath = thumbnailPath;
    }
}
