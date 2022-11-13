package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.User;
import com.Capstone.Lincall.mapper.ClientMapper;
import com.Capstone.Lincall.mapper.CounselorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    ClientMapper clientMapper;
    CounselorMapper counselorMapper;

    @Autowired
    UserService(ClientMapper clientMapper, CounselorMapper counselorMapper){
        this.clientMapper = clientMapper;
        this.counselorMapper = counselorMapper;
    }

    public boolean clientSignUp(User user){
        try{
            clientMapper.save(user);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public String clientLogIn(String id, String pw){
        User user = clientMapper.findByID(id);
        if(user == null) return "존재하지 않는 ID입니다.";
        if(user.getPassword().equals(pw)) return user.getName();
        return "비밀번호가 일치하지 않습니다.";
    }

    public boolean clientIdAvailable(String id){
        User user = clientMapper.findByID(id);
        if(user == null) return true; // 사용 가능
        else return false;  // 사용 불가
    }

    public boolean counselorSignUp(User user){
        try{
            counselorMapper.save(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String counselorLogIn(String id, String pw){
        User user = counselorMapper.findByID(id);
        if(user == null) return "존재하지 않는 ID입니다.";
        if(user.getPassword().equals(pw)) return user.getName();
        return "비밀번호가 일치하지 않습니다.";
    }

    public boolean counselorIdAvailable(String id){
        User user = counselorMapper.findByID(id);
        if(user == null) return true; // 사용 가능
        else return false;  // 사용 불가
    }
}
