package com.Capstone.Lincall.domain;

import com.Capstone.Lincall.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Room implements Comparable<Room>{
    private int roomId;
    private String client;
    private String counselor;

    private String clientName;

    private String counselorName;

    private long createTime;

    public Room(int roomId, String client, String counselor) {
        this.roomId = roomId;
        this.client = client;
        this.counselor = counselor;
        clientName = null;
        counselorName = null;
        createTime = 0;
    }

    @Override
    public boolean equals(Object o){
        if (((Room)o).getRoomId() == roomId) return true;
        return false;
    }

    @Override
    public int compareTo(Room o) {
        return (int)(createTime - o.createTime);
    }
}
