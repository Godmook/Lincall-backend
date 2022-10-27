package com.Capstone.Lincall.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Room {
    private List<WebSocketSession> list = new ArrayList<>();
    private String roomId = null;
    private boolean isProceeding = false;

    @Override
    public String toString(){
        String str = "{ roomId : "+ roomId +", cadidate : {";
        for(WebSocketSession sess : list)
            str += sess.getId() + ", ";
        str += "}";
        return str;
    }
}
