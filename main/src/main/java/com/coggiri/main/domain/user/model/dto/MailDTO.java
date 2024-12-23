package com.coggiri.main.domain.user.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema
@Getter
public class MailDTO {
    @Schema(example = "sample@naver.com",description = "이메일")
    private final String email;
    @Schema(example = "asd123asd",description = "인증번호")
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
