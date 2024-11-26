package com.coggiri.main.mvc.domain.dto;

import lombok.Getter;

@Getter
public class GroupRegisterDTO {
    private int groupType;
    private String groupName;
    private String groupIntro;
    private String groupRule;
    private String groupCondition;

    GroupRegisterDTO(int groupType,String groupName, String groupIntro,
                     String groupRule, String groupCondition){
        this.groupType = groupType;
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.groupRule = groupRule;
        this.groupCondition = groupCondition;
    }
}
