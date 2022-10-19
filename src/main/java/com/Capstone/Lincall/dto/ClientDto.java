package com.Capstone.Lincall.dto;

import lombok.Getter;

@Getter
public class ClientDto {
    private String id;
    private String password;
    private String email;
    private String name;

    @Override
    public String toString(){
         return "Client { " + "\n" +
                "   id = " + id + ", \n" +
                "   password = " + password + ", \n" +
                "   email = " + email + ", \n" +
                "   name = " + name + "\n"  +
                 "}";

    }
}
