package com.coggiri.main.jwtUtils;

import com.coggiri.main.customEnums.Role;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import com.coggiri.main.mvc.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Aspect
@Component
public class GroupAuthorizationEvaluator {

    @Autowired
    private UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Before("@annotation(requireGroupRole)")
    public void hasGroupRole(JoinPoint joinPoint, RequireGroupRole requireGroupRole) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) throw new AccessDeniedException("인증되지 않았습니다.");

        Object[] args = joinPoint.getArgs();
        String groupIdParameterName = requireGroupRole.groupIdParameter();
        int groupId = extractGroupId(args,groupIdParameterName);

        String userName = authentication.getName();
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(userInfo.isEmpty()) throw new AccessDeniedException("사용자 정보가 없습니다.");

        log.info("userId: " + userInfo.get().getId());
        log.info("groupId: " + groupId);

        Optional<UserGroupRole> userGroupRoleInfo = userRepository.findGroupRoleByUserId(userInfo.get().getId(),groupId);
        if(userGroupRoleInfo.isEmpty()) throw new AccessDeniedException("그룹에 대한 사용자 정보가 없습니다.");

        UserGroupRole userGroupRole = userGroupRoleInfo.get();
        Role userRole = Role.valueOf(userGroupRole.getRole());
        log.info("role: " + userRole);

        if(userRole.getLevel() > requireGroupRole.value().getLevel()) throw new AccessDeniedException("해당 그룹에 대한 권한이 없습니다.");
    }

    private Integer extractGroupId(Object[] args, String parameterName) {
        for (Object arg : args) {
            if (arg instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) arg;
                Object groupId = map.get(parameterName);
                if (groupId instanceof Integer) {
                    return (Integer) groupId;
                } else if (groupId instanceof String) {
                    return Integer.parseInt((String) groupId);
                }
            }

            try {
                Method method = arg.getClass().getMethod("get" +
                        parameterName.substring(0, 1).toUpperCase() +
                        parameterName.substring(1));
                return (Integer) method.invoke(arg);
            } catch (Exception e) {
                continue;
            }
        }
        throw new IllegalArgumentException("Group ID를 찾을 수 없습니다.");
    }
}