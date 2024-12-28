package com.coggiri.main.commons.jwtUtils;

import com.coggiri.main.commons.Enums.ErrorType;
import com.coggiri.main.commons.Enums.Role;
import com.coggiri.main.commons.exception.customException;
import com.coggiri.main.domain.user.model.entity.User;
import com.coggiri.main.domain.user.model.entity.UserGroupRole;
import com.coggiri.main.domain.user.repository.UserRepository;
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

        if(authentication == null || !authentication.isAuthenticated()) throw new customException(ErrorType.UNAUTHORIZED);

        Object[] args = joinPoint.getArgs();
        String groupIdParameterName = requireGroupRole.groupIdParameter();
        Long groupId = extractGroupId(args,groupIdParameterName);

        String userName = authentication.getName();
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(userInfo.isEmpty()) throw new customException(ErrorType.NOT_FOUND_USER);

        log.info("userId: " + userInfo.get().getId());
        log.info("groupId: " + groupId);

        Optional<UserGroupRole> userGroupRoleInfo = userRepository.findGroupRoleByUserId(userInfo.get().getId(),groupId);
        if(userGroupRoleInfo.isEmpty()) throw new customException(ErrorType.NOT_FOUND_USER_GROUP);

        UserGroupRole userGroupRole = userGroupRoleInfo.get();
        Role userRole = Role.valueOf(userGroupRole.getRole());
        log.info("role: " + userRole);

        if(userRole.getLevel() > requireGroupRole.value().getLevel()) throw new customException(ErrorType.UNAUTHORIZED_USER_GROUP);
    }

    private Long extractGroupId(Object[] args, String parameterName) {
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            } else if (arg instanceof String) {
                return Long.parseLong((String) arg);
            }

            try {
                Method method = arg.getClass().getMethod("get" +
                        parameterName.substring(0, 1).toUpperCase() +
                        parameterName.substring(1));
                return (Long) method.invoke(arg);
            } catch (Exception e) {
                continue;
            }
        }
        throw new customException(ErrorType.INVALID_GROUP_ID);
    }
}