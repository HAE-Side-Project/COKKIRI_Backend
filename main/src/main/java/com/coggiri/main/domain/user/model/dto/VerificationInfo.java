package com.coggiri.main.domain.user.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VerificationInfo {
    private String code;
    private LocalDateTime expireDate;
    private int attempts;

    public VerificationInfo(String code, LocalDateTime expireDate, int attempts){
        this.code = code;
        this.expireDate = expireDate;
        this.attempts = attempts;
    }

    public void addAttempts(){
        this.attempts += 1;
    }
}
