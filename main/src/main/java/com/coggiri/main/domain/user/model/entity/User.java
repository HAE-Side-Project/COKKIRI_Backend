package com.coggiri.main.domain.user.model.entity;

import com.coggiri.main.commons.Enums.Role;
import com.coggiri.main.domain.user.model.dto.request.UserCreateDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class User implements UserDetails {
    private Long id;
    private String userId;
    private String password;
    private String userName;
    private String email;
    private List<UserGroupRole> roles = new ArrayList<>();


    public User(Long id,String userId,String password,String userName,String email){
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }

    public User(Long id,String userId,String password,String userName,String email,List<UserGroupRole> roles){
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.roles = roles;
    }

    public User(UserCreateDTO registerDTO, String encodedPassword){
        this.userId = registerDTO.getUserId();
        this.password = encodedPassword;
        this.userName = registerDTO.getUserName();
        this.email = registerDTO.getEmail();
        this.roles = new ArrayList<>();
    }

    public Role getRoleForGroup(Long groupId) {
        return roles.stream()
                .filter(gr -> Long.valueOf(gr.getGroupId()).equals(groupId))
                .map(UserGroupRole::getRole)
                .map(roleStr -> Role.valueOf(roleStr.startsWith("ROLE_") ? roleStr : "ROLE_" + roleStr))
                .findFirst()
                .orElse(Role.GUEST);
    }

    public String[] getRole() {
        return roles.stream()
                .map(gr -> "ROLE_" + Role.valueOf(gr.getRole()) + "_GROUP_" + gr.getGroupId())
                .toArray(String[]::new);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.roles.stream()
                .map(gr -> "ROLE_" + Role.valueOf(gr.getRole()) + "_GROUP_" + gr.getGroupId())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Long getId(){
        return this.id;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    public String getName(){
        return this.userName;
    }

    public String getEmail(){
        return this.email;
    }

    public void setRoles(List<UserGroupRole> roles){
        this.roles = roles;
    }

    public List<UserGroupRole> getRoles(){
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}