package com.coggiri.main.domain.user.model.dto.response;

import com.coggiri.main.domain.user.model.entity.JwtToken;

public record UserInfoResponse(
    String accessToken,
    String refreshToken
) {
    public static UserInfoResponse of(JwtToken jwtToken){
        return new UserInfoResponse(
            jwtToken.getAccessToken(),
            jwtToken.getRefreshToken()
        );
    }
}
