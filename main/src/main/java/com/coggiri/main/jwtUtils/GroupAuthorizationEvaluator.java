package com.coggiri.main.jwtUtils;

import com.coggiri.main.customEnums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class GroupAuthorizationEvaluator {
    public boolean hasGroupRole(Authentication authentication, int groupId, Role requireRole){
        if(authentication == null || !authentication.isAuthenticated()) return false;

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.contains("GROUP_" + groupId))
                .map(authority -> {
                    String roleName = authority.substring(5,authority.lastIndexOf("_GROUP_"));
                    return Role.valueOf(roleName);
                })
                .anyMatch(role -> role.getLevel() <= requireRole.getLevel());
    }
}
