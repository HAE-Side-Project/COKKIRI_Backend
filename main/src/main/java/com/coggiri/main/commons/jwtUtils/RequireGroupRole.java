package com.coggiri.main.commons.jwtUtils;

import com.coggiri.main.commons.Enums.Role;

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