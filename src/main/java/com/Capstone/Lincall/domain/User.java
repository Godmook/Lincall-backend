package com.Capstone.Lincall.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
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
