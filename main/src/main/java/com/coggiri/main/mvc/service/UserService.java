package com.coggiri.main.mvc.service;

import com.coggiri.main.customEnums.Role;
import com.coggiri.main.mvc.domain.dto.UserDTO;
import com.coggiri.main.mvc.domain.dto.UserLoginDTO;
import com.coggiri.main.mvc.domain.entity.JwtToken;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import com.coggiri.main.mvc.repository.UserRepository;
import com.coggiri.main.jwtUtils.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    public void register(UserDTO userInfo){
        if(userRepository.findByUsername(userInfo.getUserId()).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 사용자 아이디입니다.");
        }

        String encodePassword = passwordEncoder.encode(userInfo.getPassword());
        List<UserGroupRole> roles = new ArrayList<>();

        User user = userInfo.toUser(encodePassword,roles);

        if(userRepository.register(user) == 0){
            throw new IllegalArgumentException("회원 정보 데이터베이스 저장 실패");
        }

        if(addUserRole(new UserGroupRole(user.getId(),0, Role.USER.name())) == 0){
            throw new IllegalArgumentException("회원 권한 데이터베이스 저장 실패");
        }
    }

    public void deleteUser(int userId){
        log.info("userId: " + userId);
        if(userRepository.deleteUser(userId) < 1){
            throw new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다.");
        }
    }

    public int addUserRole(UserGroupRole userGroupRole){
        return userRepository.addUserRole(userGroupRole);
    }

    public void deleteUserRoleByGroupId(int groupId){
        userRepository.deleteUserRoleByGroupId(groupId);
    }

    public JwtToken login(String userId, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId,password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    public void changePassword(UserLoginDTO userLoginDTO){
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
