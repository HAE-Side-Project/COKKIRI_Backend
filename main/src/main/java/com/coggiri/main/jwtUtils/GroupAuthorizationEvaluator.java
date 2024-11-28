package com.coggiri.main.jwtUtils;

import com.coggiri.main.customEnums.Role;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import com.coggiri.main.mvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GroupAuthorizationEvaluator {
    @Autowired
    private UserRepository userRepository;

    public boolean hasGroupRole(Authentication authentication, int groupId, Role requireRole){
        if(authentication == null || !authentication.isAuthenticated()) return false;

        String userName = authentication.getName();

        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(userInfo.isEmpty()) return false;

        Optional<UserGroupRole> userGroupRoleInfo = userRepository.findGroupRoleByUserId(userInfo.get().getId(),groupId);
        if(userGroupRoleInfo.isEmpty()) return false;

        UserGroupRole userGroupRole = userGroupRoleInfo.get();
        Role userRole = Role.valueOf(userGroupRole.getRole());
        return userRole.getLevel() <= requireRole.getLevel();
    }
}