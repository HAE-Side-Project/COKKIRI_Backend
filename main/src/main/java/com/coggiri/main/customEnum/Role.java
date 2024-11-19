package com.coggiri.main.customEnum;

import lombok.Getter;

@Getter
public enum Role {
    SUPER_ADMIN(0),
    ADMIN(1),
    MANAGER(2),
    USER(3),
    GUEST(4);

    private int level;

    Role(int level){
        this.level = level;
    }

    public static boolean hasGuestPermission(Role role){
        return role.level <= GUEST.level;
    }

    public static boolean hasUserPermission(Role role){
        return role.level <= USER.level;
    }

    public static boolean hasManagerPermission(Role role){
        return role.level <= MANAGER.level;
    }

    public static boolean hasAdminPermission(Role role){
        return role.level <= ADMIN.level;
    }

    public static boolean hasSuperAdminPermission(Role role){
        return role.level <= SUPER_ADMIN.level;
    }
}
