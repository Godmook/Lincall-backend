package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.domain.Client;
import com.Capstone.Lincall.service.EmailService;
import com.Capstone.Lincall.service.RoomService;
import com.Capstone.Lincall.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping(value="/user")
public class UserController {

    UserService userService;
    RoomService roomService;
    EmailService emailService;

    @Autowired
    UserController(UserService userService, RoomService roomService, EmailService emailService){

        this.userService = userService;
        this.roomService = roomService;
        this.emailService = emailService;
    }

    @PostMapping("/client/signup")
    @ResponseBody
    public boolean clientSignUp(@RequestBody Client client){
        return userService.clientSignUp(client);
    }

    @PostMapping("/client/logIn")
    @ResponseBody
    public boolean clientLogIn(@RequestBody LogInModel model){
        return userService.clientLogIn(model.getId(), model.getPassword());
    }
    @Getter
    static class LogInModel{
        private String id;
        private String password;
    }

    // webRTC room 생성 test code
    @PostMapping("/rooms-test")
    @ResponseBody
    public List<String> clientSignUp(){
        return roomService.getAvailableRooms();
    }

    @GetMapping("/email-auth")
    @ResponseBody
    public String mailCheck(String email){
        System.out.println("이메일 인증 : " + email);
        return emailService.sendEmail(email);
    }
}
