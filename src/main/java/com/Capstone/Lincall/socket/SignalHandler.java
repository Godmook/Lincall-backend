package com.Capstone.Lincall.socket;

import com.Capstone.Lincall.domain.WebSocketMessage;
import com.Capstone.Lincall.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class SignalHandler extends TextWebSocketHandler {

    RoomService roomService;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    public SignalHandler(RoomService roomService) {
        this.roomService = roomService;
    }

    private static List<WebSocketSession> list = new ArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WebSocketMessage webSocketMessage = objectMapper.readValue(message.getPayload(), WebSocketMessage.class);
        String roomId = webSocketMessage.getRoomId();

        switch (webSocketMessage.getType())
        {
            case "join":
                if(roomService.addParticipant(session, roomId) == false) break;
                for(WebSocketSession sess : roomService.findRoomById(roomId).getList()){
                    if(sess.getId().equals(session.getId())) continue;
                    sess.sendMessage(message);
                }
                break;
            case "leave":
                if(roomService.removeParticipant(session, roomId) == false) break;
                for(WebSocketSession sess : roomService.findRoomById(roomId).getList()){
                    if(sess.getId().equals(session.getId())) continue;
                    sess.sendMessage(message);
                }
                break;
            case "offer":
            case "answer":
            case "ice":
                for(WebSocketSession sess : roomService.findRoomById(roomId).getList()){
                    if(sess.getId().equals(session.getId())) continue;
                    sess.sendMessage(message);
                }
                break;
        }

    }

    // client 접속 시 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);
    }

    // client 접속 해제 시 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        list.remove(session);
    }
}
