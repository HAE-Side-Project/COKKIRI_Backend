package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.dto.UserRegisterDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    int register(UserRegisterDTO userRegisterDTO);
}
