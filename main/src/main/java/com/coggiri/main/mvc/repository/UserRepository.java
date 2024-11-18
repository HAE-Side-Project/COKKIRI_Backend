package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    int register(User userRegisterDTO);
    Optional<User> findByUsername(String userId);
    Optional<User> findByEmail(String email);
}
