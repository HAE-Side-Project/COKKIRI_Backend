package com.coggiri.main.domain.tag.model.entity;

import lombok.Getter;

@Getter
public class Tag {
    private int tagId;
    private String[] tagNames;

    public Tag(int tagId,String[] tagNames){
        this.tagId = tagId;
        this.tagNames = tagNames;
    }

    public Tag(String[] tagNames){
        this.tagNames = tagNames;
    }
}
