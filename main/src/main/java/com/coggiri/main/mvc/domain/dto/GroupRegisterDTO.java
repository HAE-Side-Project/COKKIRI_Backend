package com.coggiri.main.mvc.domain.dto;

import lombok.Getter;

@Getter
public class GroupRegisterDTO {
    private int groupCategory;
    private String groupName;
    private String groupIntro;
    private String groupRule;
    private String groupCondition;

    GroupRegisterDTO() {

    }

    GroupRegisterDTO(int groupCategory,String groupName, String groupIntro,
                     String groupRule, String groupCondition){
        this.groupCategory = groupCategory;
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.groupRule = groupRule;
        this.groupCondition = groupCondition;
    }
}
