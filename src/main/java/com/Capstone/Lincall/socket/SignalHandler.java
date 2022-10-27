package com.Capstone.Lincall.socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class SignalHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> list = new ArrayList<>();
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String paylod = message.getPayload();
        System.out.println("[socket 통신] : " + paylod);

        for(WebSocketSession sess : list)
            sess.sendMessage(message);
    }

    // client 접속 시 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);

        System.out.println("클라이언트 접속 : " + session.getId());
    }

    // client 접속 해제 시 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        list.remove(session);

        System.out.println("클라이언트 접속 해제 : " + session.getId());
    }
}
