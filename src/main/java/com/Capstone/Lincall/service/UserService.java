package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.Client;
import com.Capstone.Lincall.mapper.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        } catch (Exception e) {
            return "회원가입 실패 \n" + e.getMessage();
        }

    }
}
