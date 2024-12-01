package com.coggiri.main.mvc.domain.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema
@Getter
public class JwtToken {
    @Schema(example = "Bearer",description = "JWT 생성 방식", defaultValue = "Bearer")
    private String grantType;
    @Schema(example = "asdg1sad23d",description = "JWT 토큰")
    private String accessToken;
    @Schema(example = "asxc1sxc3s2",description = "JWT 재발급 토큰")
    private String refreshToken;

    public JwtToken(String grantType, String accessToken, String refreshToken){
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
