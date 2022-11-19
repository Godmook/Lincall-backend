package com.Capstone.Lincall.socket;

import com.Capstone.Lincall.controller.ConsultingController;
import com.Capstone.Lincall.domain.ConsultingView;
import com.Capstone.Lincall.domain.Room;
import com.Capstone.Lincall.domain.WebSocketMessage;
import com.Capstone.Lincall.service.ConsultingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {
    public List<Room> roomList =new ArrayList<>();
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    @Autowired private ConsultingService consultingService;

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
            // counselor join
            r.setCounselor(message.getSender());
            consultingService.updateConsultingUser(r.getRoomId(), r.getCounselor(), r.getClient());
        }else{
            // client join - room create
            r.setClient(message.getSender());
        }

        // roomId 채널을 구독 중인 사람에게 join 발생 알림
        simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), message.getSender() + "join");
    }

    @MessageMapping("/data")
    public void sendData(WebSocketMessage message){
        // roomId 채널을 구독 중인 사람에게 전달
        simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), message);
    }

    @MessageMapping("/success")
    public void connectinSuccess(WebSocketMessage message){
        Room r = findRoomById(message.getChannelId());
        if(r != null) roomList.remove(r);
        // roomId 채널을 구독 중인 사람에게 전달
        simpMessageSendingOperations.convertAndSend("/sub/room/" + message.getChannelId(), message);
    }


}
