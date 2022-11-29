package com.Capstone.Lincall.socket;

import com.Capstone.Lincall.domain.Room;
import com.Capstone.Lincall.service.ConsultingService;
import com.Capstone.Lincall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.jsse.JSSEImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {
    public List<Room> roomList =new ArrayList<>();     // 고객 대기 방
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    @Autowired private ConsultingService consultingService;
    @Autowired private UserService userService;

    public Room findRoomById(int id){
        for(Room r : roomList)
            if(r.getRoomId() == id)
                return r;
        return null;
    }

    @MessageMapping("/join")
    public void joinRoom(WebSocketMessage message){
        Room r = findRoomById(message.getChannelId());

        if(r == null){
            simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), "방이 존재하지 않습니다.");
            return;
        }

        if(message.getType().equals("counselor")){
            // 이미 매칭된 counselor 존재
            if(r.getCounselor() != null){
                simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), "이미 상담사가 매칭된 방입니다.");
                return;
            }
            // counselor join
            r.setCounselor(message.getSender());
            r.setCounselorName(userService.getCounselor(message.getSender()).getName());
            consultingService.updateConsultingUser(r.getRoomId(), r.getCounselor(), r.getClient());
        }else{
            // client join - room create
            r.setClient(message.getSender());
            r.setClientName(userService.getClient(message.getSender()).getName());
            r.setCreateTime(System.currentTimeMillis());
        }

        // roomId 채널을 구독 중인 사람에게 join 발생 알림
        simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), message.getSender() + " join");
    }

    @MessageMapping("/data")
    public void sendData(WebSocketMessage message){
        // roomId 채널을 구독 중인 사람에게 전달
        simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), message);
    }

    @MessageMapping("/success")
    public void connectionSuccess(WebSocketMessage message){
        Room r = findRoomById(message.getChannelId());
        if(r != null) roomList.remove(r);

        // 상담 시작 시간 업데이트
        consultingService.startConsulting(String.valueOf(message.getChannelId()));

        // roomId 채널을 구독 중인 사람에게 전달
        simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), message);
    }

    @MessageMapping("/end")
    public void endConnection(WebSocketMessage message){
        // 상담 종료 시간 업데이트
        consultingService.endConsulting(String.valueOf(message.getChannelId()));

        // roomId 채널을 구독 중인 사람에게 전달
        simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), "consulting end");
    }

    @MessageMapping("/quit")
    public void quit(WebSocketMessage message){
        Room r = findRoomById(message.getChannelId());
        if(r != null) roomList.remove(r);

        // 상담 기록 db에서 삭제
        consultingService.deleteConsulting(String.valueOf(message.getChannelId()));

        // roomId 채널을 구독 중인 사람에게 전달
        simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId() ,message.getSender() + " quit");
    }

    public void sendMessage(int roomId, String message){
        simpMessageSendingOperations.convertAndSend("/sub/room/" + roomId, message);
    }

}
