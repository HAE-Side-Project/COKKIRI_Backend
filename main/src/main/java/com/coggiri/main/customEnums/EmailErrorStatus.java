package com.coggiri.main.customEnums;

import lombok.Getter;

@Getter
public enum EmailErrorStatus {
    NOT_EXIST(0),
    EXPIRED(1),
    ILLEGAL_ACCESS(2),
    VALID(3),
    NOT_EQUAL(4);

    private int val;

    EmailErrorStatus(int val){
        this.val = val;
    }
}
