package com.Capstone.Lincall.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Consulting {
    private int id;
    private String counselor;
    private String client;
    private long start;
    private long end;

    @Override
    public String toString(){
        return "Consulting { " + "\n" +
                "   id = " + id + ", \n" +
                "   counselor id = " + counselor + ", \n" +
                "   client id = " + client + ", \n" +
                "   start time = " + start + "\n"  +
                "   end time = " + end + "\n"  +
                "}";

    }
}
