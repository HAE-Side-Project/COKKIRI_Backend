package com.coggiri.main.customEnums;

import lombok.Getter;

@Getter
public enum TagType {
    TASK(0),
    GROUP(1);

    private int type;

    TagType(int type){
        this.type = type;
    }
}
