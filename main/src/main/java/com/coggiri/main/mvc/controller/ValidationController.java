package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.domain.dto.MailDTO;
import com.coggiri.main.mvc.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/check")
public class ValidationController {
    private MailService mailService;

    @Autowired
    ValidationController(MailService mailService){
        this.mailService = mailService;
    }

    @ResponseBody
    @PostMapping("/emailValidation")
    public String emailValidation(@RequestBody MailDTO mailDTO) throws MessagingException, UnsupportedEncodingException{
        System.out.println("mail Controller");
        String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());
        return authCode;
    }
}
