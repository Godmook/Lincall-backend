package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.domain.Client;
import com.Capstone.Lincall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping(value="/user")
public class UserController {

    UserService userService;

    @Autowired
    UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/client/signup")
    @ResponseBody
    public String clientSignUp(@RequestBody Client client){
        return userService.clientSignUp(client);
    }

    // flask 연동 test code
    @PostMapping("/client/findByID")
    @ResponseBody
    public Client clientFind(@RequestBody String id){
        return userService.clientFindByID(id);
    }

    @PostMapping("/flask-test")
    @ResponseBody
    public String flaskTest(@RequestBody String str){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:5000")
                .encode()
                .build()
                .toUri();
        System.out.println(uri.toString());

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        System.out.println(result.getStatusCode());
        System.out.println(result.getBody());
        return result.getBody();
    }

}
