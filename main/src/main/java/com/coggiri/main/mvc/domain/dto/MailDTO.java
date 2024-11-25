package com.coggiri.main.mvc.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MailDTO {
    private final String email;
    private String authCode = "";

    @JsonCreator
    public MailDTO(@JsonProperty("email") String email) {  // @JsonProperty로 JSON 필드명 매핑
        this.email = email;
    }


    public MailDTO(String email, String authCode){
        this.email = email;
        this.authCode = authCode;
    }
}
