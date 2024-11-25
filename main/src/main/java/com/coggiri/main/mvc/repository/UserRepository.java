package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    int register(User userRegisterDTO);
    int addUserRole(UserGroupRole userGroupRole);
    int changePassword(User user);
    Optional<User> findByUsername(String userId);
    Optional<User> findByEmail(String email);
    List<UserGroupRole> findGroupRolesByUserId(int userId);
}
