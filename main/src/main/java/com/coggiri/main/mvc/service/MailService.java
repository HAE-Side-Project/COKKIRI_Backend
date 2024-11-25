package com.coggiri.main.mvc.service;

import com.coggiri.main.customEnums.EmailErrorStatus;
import com.coggiri.main.mvc.domain.dto.VerificationInfo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "woodevelop3@gmail.com";
    private final Map<String, VerificationInfo> verificationInfoCache = new ConcurrentHashMap<>();

    public String createNumber(){
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for(int i = 0; i < 8; i++){
            int index = random.nextInt(3);

            switch (index){
                case 0 -> key.append((char) (random.nextInt(26) + 97));
                case 1 -> key.append((char) (random.nextInt(26) + 65));
                case 2 -> key.append(random.nextInt(10));
            }
        }

        return key.toString();
    }

    public MimeMessage createMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO,mail);
        message.setSubject("이메일 인증");

        String body = "";
        body += "<h3>요청하신 인증 번호입니다.</h3>";
        body += "<h1>" + number + "</h1>";
        body += "<h3>감사합니다.</h3>";
        message.setText(body,"UTF-8","html");

        return message;
    }

    public VerificationInfo sendSimpleMessage(String sendEmail) throws MessagingException{
        String number = createNumber();

        MimeMessage message = createMail(sendEmail, number);
        try {
            javaMailSender.send(message);
        }catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
        }

        LocalDateTime limitTime = LocalDateTime.now().plusMinutes(5);
        VerificationInfo ret = new VerificationInfo(number, limitTime,0);
        verificationInfoCache.put(sendEmail,new VerificationInfo(number, limitTime,0));

        return ret;
    }

    public EmailErrorStatus verifyCode(String email, String code){
        VerificationInfo info = verificationInfoCache.get(email);
        int ret = 0;
        if(info == null) return EmailErrorStatus.NOT_EXIST;

        if(LocalDateTime.now().isAfter(info.getExpireDate())){
            verificationInfoCache.remove(email);
            return EmailErrorStatus.EXPIRED;
        }

        info.addAttempts();

        if(info.getAttempts() >= 5){
            verificationInfoCache.remove(email);
            return EmailErrorStatus.ILLEGAL_ACCESS;
        }

        boolean isValid = code.equals(info.getCode());
        if(isValid) {
            verificationInfoCache.remove(email);
            return EmailErrorStatus.VALID;
        }

        return EmailErrorStatus.NOT_EQUAL;
    }

    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredCodes(){
        if(verificationInfoCache.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();
        verificationInfoCache.entrySet().removeIf(entry ->
                now.isAfter(entry.getValue().getExpireDate()));
    }
}
