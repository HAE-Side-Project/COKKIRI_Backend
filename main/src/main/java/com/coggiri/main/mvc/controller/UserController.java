package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

//    @PostMapping("checkDuplicate")
//    public ResponseEntity<Map<String,Object>> checkDuplicateId(@RequestBody Map<String,String> userId){
//
//        return ResponseEntity.ok();
//    }
    @PostMapping("register")
    public ResponseEntity<Map<String, Objects>> register(@RequestBody Map<String,String> userInfo) {
        Map<String, Objects> response = new HashMap<>();

        try {
            boolean ret = userService.register(userInfo);
            if (ret) {
                response.put("success", true);
            } else {
                response.put("success", true);
//                response.put("message","register Failed");
            }
        } catch (Exception e) {
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
