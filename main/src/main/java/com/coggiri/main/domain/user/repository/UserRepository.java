package com.coggiri.main.domain.user.repository;

import com.coggiri.main.domain.user.model.entity.User;
import com.coggiri.main.domain.user.model.entity.UserGroupRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    int register(User userRegisterDTO);
    int delete(Long userId);
    int addUserRole(UserGroupRole userGroupRole);
    void deleteUserRoleByGroupId(Long groupId);
    void changePassword(User user);
    Optional<User> findByUsername(String userId);
    Optional<User> findByUserId(Long userId);
    Optional<User> findByEmail(String email);
    List<UserGroupRole> findGroupRolesByUserId(Long userId);
    Optional<UserGroupRole> findGroupRoleByUserId(@Param("userId") Long userId,@Param("groupId") Long groupId);
}
