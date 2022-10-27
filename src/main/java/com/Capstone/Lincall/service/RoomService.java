package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.Room;
import com.Capstone.Lincall.domain.WebSocketMessage;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class RoomService {
    private HashMap<String,Room> rooms = new HashMap<>();

    public boolean addParticipant(WebSocketSession participant, String roomId){
        // 방 있는지 검사
        Room room = findRoomById(roomId);
        if(room == null)
            room = new Room();
        // 상담이 진행 중 인지 검사
        if(room.isProceeding() == true)
            return false;
        // 방 생성 또는 방 참가자 추가
        room.setRoomId(roomId);
        room.getList().add(participant);
        rooms.put(roomId, room);
        // 상담이 시작 된 경우
        if(room.getList().size() == 2)
            room.setProceeding(true);
        return true;
    }

    public boolean removeParticipant(WebSocketSession participant, String roomId){
        Room room = findRoomById(roomId);
        if(room == null) return false;

        room.getList().remove(participant);
        if(room.getList().size() == 0)
            rooms.remove(roomId);
        else
            room.setProceeding(false);
        return true;
    }

    public Room findRoomById(String roomId){
        return rooms.get(roomId);
    }

    // test code
    public List<String> getAvailableRooms(){
        List<String> list = new ArrayList<>();
        for(Room r : rooms.values()){
            if(r.isProceeding() == false)
                list.add(r.toString());
        }
        return list;
    }
}
