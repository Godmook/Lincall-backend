package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.domain.ConsultingView;
import com.Capstone.Lincall.domain.Room;
import com.Capstone.Lincall.service.ConsultingService;
import com.Capstone.Lincall.socket.WebSocketMessageController;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value="/consulting")
@CrossOrigin(origins = "*")
public class ConsultingController {
    ConsultingService consultingService;
    WebSocketMessageController webSocketMessageController;

    public ConsultingController(ConsultingService consultingService, WebSocketMessageController webSocketMessageController){
        this.consultingService = consultingService;
        this.webSocketMessageController = webSocketMessageController;
    }

    @Getter
    static class createModel{
        private String counselor;
        private String client;
    }
    @GetMapping("/create")
    @ResponseBody
    public int createNewConsulting(){
        int id = consultingService.createConsulting(null, null);
        webSocketMessageController.roomList.add(new Room(id, null, null));
        return id;

    }

    @GetMapping("/end")
    @ResponseBody
    public void endConsulting(String id){
        consultingService.endConsulting(id);
    }

    @GetMapping("/records")
    @ResponseBody
    public List<ConsultingView> getConsultingRecord(String clientID){
        return consultingService.getConsultingsByClient(clientID);
    }

    @GetMapping("/room-list")
    @ResponseBody
    public List<Room> getRoomList(){
        return webSocketMessageController.roomList;
    }

}
