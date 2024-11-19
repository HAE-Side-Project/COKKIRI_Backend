package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.domain.dto.MailDTO;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.service.MailService;
import com.coggiri.main.mvc.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/validation")
public class ValidationController {
    private final MailService mailService;
    private final UserService userService;

    @Autowired
    ValidationController(MailService mailService, UserService userService){
        this.mailService = mailService;
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/email")
    public ResponseEntity<Map<String,Object>> emailValidation(@RequestBody MailDTO mailDTO) throws MessagingException, UnsupportedEncodingException{
        Map<String, Object> response = new HashMap<>();
        try{
            if(userService.findUserByEmail(mailDTO.getEmail()).isPresent()){
                throw new IllegalArgumentException("이미 인증에 사용된 이메일입니다.");
            }
            String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());
            response.put("success",true);
            response.put("authCod",authCode);
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/Id")
    public ResponseEntity<Map<String,Object>> idValidation(@RequestBody String userId){
        Map<String, Object> response = new HashMap<>();
        if(userService.findUserById(userId).isPresent()){
            response.put("success",true);
            response.put("userId",userId);
        }else{
            response.put("success",false);
        }

        return ResponseEntity.ok(response);
    }
}
