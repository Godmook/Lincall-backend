package com.Capstone.Lincall.service;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public String sendEmail(String email){
        String pwd = makeRandomString();
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email);
        smm.setSubject("lincall 인증 메일");
        smm.setText("Lincall\n" +
                "메일 인증 안내 입니다 .\n" +
                "\n" +
                "안녕하세요.\n" +
                "Lincall을 이용해 주셔서 진심으로 감사드립니다. \n" +
                "아래 메일 인증 키를 입력하여 회원가입을 완료해 주세요.\n" +
                "감사합니다. \n" +
                "\n" + pwd);

        // mail test code
        System.out.println(smm);
        //mailSender.send(smm);

        return pwd;
    }

    public String makeRandomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println(generatedString);
        return generatedString;
    }

}
