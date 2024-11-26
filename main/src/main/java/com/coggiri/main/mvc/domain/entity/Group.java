package com.coggiri.main.mvc.domain.entity;

import lombok.Getter;

@Getter
public class Group {
    private int groupId;
    private int groupType;
    private String groupName;
    private String groupIntro;
    private String groupRule;
    private String groupCondition;
    private String thumbnailPath;


    Group(int groupId, int groupType, String groupName, String groupIntro,
          String groupRule, String groupCondition, String thumbnailPath){
        this.groupId = groupId;
        this.groupType = groupType;
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.groupRule = groupRule;
        this.groupCondition =groupCondition;
        this.thumbnailPath = thumbnailPath;
    }
}
