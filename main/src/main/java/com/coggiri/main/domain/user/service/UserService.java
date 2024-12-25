package com.coggiri.main.domain.user.service;

import com.coggiri.main.commons.Enums.ErrorType;
import com.coggiri.main.commons.Enums.Role;
import com.coggiri.main.commons.exception.customException;
import com.coggiri.main.domain.user.model.dto.request.UserLoginDTO;
import com.coggiri.main.domain.user.model.dto.request.UserCreateDTO;
import com.coggiri.main.domain.user.model.dto.response.UserInfoResponse;
import com.coggiri.main.domain.user.model.entity.JwtToken;
import com.coggiri.main.domain.user.model.entity.User;
import com.coggiri.main.domain.user.model.entity.UserGroupRole;
import com.coggiri.main.domain.user.repository.UserRepository;
import com.coggiri.main.commons.jwtUtils.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class UserService{
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userRepository,AuthenticationManagerBuilder authenticationManagerBuilder,
                JwtTokenProvider jwtTokenProvider,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(UserCreateDTO userCreateDTO){
        if(userRepository.findByUsername(userCreateDTO.getUserId()).isPresent()){
            throw new customException(ErrorType.INVALID_USER_CREATE_ID);
        }

        String encodePassword = passwordEncoder.encode(userCreateDTO.getPassword());

        User user = new User(userCreateDTO,encodePassword);

        if(userRepository.register(user) == 0){
            throw new customException(ErrorType.INTERNAL_SERVER);
        }

        if(addUserRole(new UserGroupRole(user.getId(),1, Role.USER.name())) == 0){
            throw new customException(ErrorType.INTERNAL_SERVER);
        }
    }

    public void delete(String token){
        Claims claims = jwtTokenProvider.parseClaims(token);
        Long userId = claims.get("userId",Long.class);

        if(userRepository.delete(userId) < 1){
            throw new customException(ErrorType.NOT_FOUND_USER);
        }
    }

    public int addUserRole(UserGroupRole userGroupRole){
        return userRepository.addUserRole(userGroupRole);
    }

    public void deleteUserRoleByGroupId(int groupId){
        userRepository.deleteUserRoleByGroupId(groupId);
    }

    public UserInfoResponse login(String userId, String password){
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
            return UserInfoResponse.of(jwtToken);
        }catch (IllegalArgumentException e){
            throw new customException(ErrorType.INVALID_USER_LOGIN_REQUEST);
        }catch (AuthenticationException e){
            throw new customException(ErrorType.UNAUTHORIZED);
        }
    }

    public void changePassword(UserLoginDTO userLoginDTO,String token){
        Claims claims = jwtTokenProvider.parseClaims(token);
        Long userId = claims.get("userId",Long.class);

        User user = findUserById(userLoginDTO.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다. "));
        try{
            String encodeNextPassword = passwordEncoder.encode(userLoginDTO.getPassword());
            User nextUser = new User(user.getId(),user.getUsername(),encodeNextPassword,user.getName(),user.getEmail());
            userRepository.changePassword(nextUser);
        }catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    public Optional<User> findUserById(String userId){
        return userRepository.findByUsername(userId);
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
