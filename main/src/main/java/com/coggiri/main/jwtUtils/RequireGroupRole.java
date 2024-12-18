package com.coggiri.main.jwtUtils;

import org.springframework.security.access.prepost.PreAuthorize;
import com.coggiri.main.customEnums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireGroupRole {
    Role value();
    String groupIdParameter() default "groupId";
}