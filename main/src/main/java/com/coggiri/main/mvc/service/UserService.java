package com.coggiri.main.mvc.service;

import com.coggiri.main.mvc.domain.dto.UserRegisterDTO;
import com.coggiri.main.mvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean register(UserRegisterDTO user){
        return userRepository.register(user) > 0;
    }
}
