package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.Client;
import com.Capstone.Lincall.mapper.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class UserService {
    ClientMapper clientMapper;

    @Autowired
    UserService(ClientMapper clientMapper){
        this.clientMapper = clientMapper;
    }

    public String clientSignUp(Client client){
        try{
            clientMapper.save(client);
            return "회원가입 성공";
        } catch (Exception e){
            return "회원가입 실패";
        }
    }

    public Client clientFindByID(String id){
        return clientMapper.findByID(id);
    }
}
