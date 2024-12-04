package com.coggiri.main.mvc.domain.entity;

import lombok.Getter;

@Getter
public class Tag {
    private int tagId;
    private String tagName;
    private int groupId;
    private int taskId;

    Tag(int tagId,String tagName,int groupId,int taskId){
        this.tagId = tagId;
        this.tagName = tagName;
        this.groupId = groupId;
        this.taskId = taskId;
    }
}
