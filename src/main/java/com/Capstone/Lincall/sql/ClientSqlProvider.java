package com.Capstone.Lincall.sql;

import com.Capstone.Lincall.domain.Client;

public class ClientSqlProvider {
    public String saveSql(Client client){
        return "INSERT INTO client VALUES( '" + client.getId()+ "' , '" + client.getPassword() + "' , '" +  client.getEmail() + "' , '" +  client.getName() + "' );";
    }

    public String findByID(String id){
        return "SELECT * from client WHERE id = '" + id + "';";
    }
}
