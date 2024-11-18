package com.coggiri.main.mvc.service;

import com.coggiri.main.mvc.domain.dto.UserDTO;
import com.coggiri.main.mvc.domain.entity.JwtToken;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.repository.UserRepository;
import com.coggiri.main.provider.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService{
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

    public void register(UserDTO userInfo){
        if(userRepository.findByUsername(userInfo.getUserId()).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 사용자 아이디입니다.");
        }

        String encodePassword = passwordEncoder.encode(userInfo.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        User user = userInfo.toUser(encodePassword,roles);
        if(userRepository.register(user) == 0){
            throw new IllegalArgumentException("데이터베이스 저장 실패");
        }
    }

    public JwtToken login(String userId, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId,password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    public Optional<User> findUserById(String userId){
        return userRepository.findByUsername(userId);
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
