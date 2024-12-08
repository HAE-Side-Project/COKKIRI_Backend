package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    int register(User userRegisterDTO);
    int deleteUser(int userId);
    int addUserRole(UserGroupRole userGroupRole);
    void deleteUserRoleByGroupId(int groupId);
    int changePassword(User user);
    Optional<User> findByUsername(String userId);
    Optional<User> findByEmail(String email);
    List<UserGroupRole> findGroupRolesByUserId(int userId);
    Optional<UserGroupRole> findGroupRoleByUserId(@Param("userId") int userId,@Param("groupId") int groupId);
}
