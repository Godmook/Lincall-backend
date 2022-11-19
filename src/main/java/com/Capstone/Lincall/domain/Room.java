package com.Capstone.Lincall.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Room {
    private int roomId;
    private String client;
    private String counselor;

    public Room(int roomId, String client, String counselor) {
        this.roomId = roomId;
        this.client = client;
        this.counselor = counselor;
    }

    @Override
    public boolean equals(Object o){
        if (((Room)o).getRoomId() == roomId) return true;
        return false;
    }
}
