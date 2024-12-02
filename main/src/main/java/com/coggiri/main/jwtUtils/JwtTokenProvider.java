package com.coggiri.main.jwtUtils;

import com.coggiri.main.mvc.domain.entity.JwtToken;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import com.coggiri.main.mvc.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final UserRepository userRepository;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

    @Autowired
    public JwtTokenProvider(@Value("${jwt.token.key}") String secretKey, UserRepository userRepository){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userRepository = userRepository;
    }

    public JwtToken generateToken(Authentication authentication){

        com.coggiri.main.mvc.domain.entity.User userInfo = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        long now = (new Date()).getTime();

        Date accessTokenExpiresln = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("userId",userInfo.getId())
                .setExpiration(accessTokenExpiresln)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();

        return new JwtToken("Bearer",accessToken,refreshToken);
    }

    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);
        String userName = claims.getSubject();
        Integer userId = claims.get("userId",Integer.class);

        List<UserGroupRole> userRoles = userRepository.findGroupRolesByUserId(userId);

        if(userRoles.isEmpty()){
            throw new RuntimeException("권한이 없습니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                userRoles.stream()
                        .map(role -> new SimpleGrantedAuthority(
                                "ROLE_" + role.getRole() + "_GROUP_" + role.getGroupId()
                        ))
                        .collect(Collectors.toList());

        UserDetails principal = new User(userName,"",authorities);
        return new UsernamePasswordAuthenticationToken(principal,"",authorities);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT Token", e);
        }catch (ExpiredJwtException e){
            log.info("Expired JWT Token",e);
        }catch (UnsupportedJwtException e){
            log.info("Unsupported JWT Token",e);
        }catch (IllegalArgumentException e){
            log.info("JWT claims string is empty",e);
        }

        return false;
    }

    private Claims parseClaims (String accessToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}