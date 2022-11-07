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

    public boolean clientSignUp(Client client){
        try{
            clientMapper.save(client);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public String clientLogIn(String id, String pw){
        Client client = clientMapper.findByID(id);
        if(client == null) return null;
        return client.getName();
    }

    public boolean IdAvailable(String id){
        Client client = clientMapper.findByID(id);
        if(client == null) return true;
        else return false;
    }
}
