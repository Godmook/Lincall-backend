package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.dto.ClientDto;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.http.HttpHeaders;

@Controller
@RequestMapping(value="/user")
public class UserController {

    @PostMapping("/client/signup")
    public void clientSignUp(@RequestBody ClientDto clientDto){
        System.out.println(clientDto);
    }
}
