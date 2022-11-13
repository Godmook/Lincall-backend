package com.Capstone.Lincall.sql;

import com.Capstone.Lincall.domain.User;

public class CounselorSqlProvider {
    public String saveSql(User user){
        return "INSERT INTO counselor VALUES( '" + user.getId()+ "' , '" + user.getPassword() + "' , '" +  user.getEmail() + "' , '" +  user.getName() + "' );";
    }

    public String findByID(String id){
        return "SELECT * from counselor WHERE id = '" + id + "';";
    }
}
