package com.Capstone.Lincall.sql;

import com.Capstone.Lincall.domain.User;

public class ClientSqlProvider {
    public String saveSql(User user){
        return "INSERT INTO client VALUES( '" + user.getId()+ "' , '" + user.getPassword() + "' , '" +  user.getEmail() + "' , '" +  user.getName() + "' );";
    }

    public String findByID(String id){
        return "SELECT * from client WHERE id = '" + id + "';";
    }
}
