package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.domain.dto.UserLoginDTO;
import com.coggiri.main.mvc.domain.dto.UserDTO;
import com.coggiri.main.mvc.domain.entity.JwtToken;
import com.coggiri.main.mvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private UserService userService;

    @Autowired
    UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("test")
    public String test(){
        return "success";
    }

    @PostMapping("login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody UserLoginDTO userLoginDTO){
        Map<String, Object> response = new HashMap<>();
        try {
            JwtToken jwtToken = userService.login(userLoginDTO.getUserId(), userLoginDTO.getPassword());
            log.info("request username = {}, password = {}", userLoginDTO.getUserId(), userLoginDTO.getPassword());
            log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
            response.put("success",true);
            response.put("JwtToken",jwtToken);
            response.put("userId", userLoginDTO.getUserId());
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDTO userInfo) {
        Map<String, Object> response = new HashMap<>();

        try {
            userService.register(userInfo);
            response.put("success", true);
            response.put("message","register Success");
        } catch (Exception e) {
            response.put("success",false);
            response.put("message", (e.getMessage() != null) ? e.getMessage() : "Unknown error occurred");
        }

        return ResponseEntity.ok(response);
    }
}
