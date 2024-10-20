package com.coggiri.main.mvc.domain;

import lombok.Getter;

@Getter
public class Group {
    private int groupId;
    private String groupName;
    private String groupIntro;
    private String groupRule;
    private String groupCondition;


    Group(int groupId, String groupName, String groupIntro, String groupRule,String groupCondition){
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.groupRule = groupRule;
        this.groupCondition =groupCondition;
    }
}
