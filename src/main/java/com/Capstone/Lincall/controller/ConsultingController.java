package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.domain.ConsultingView;
import com.Capstone.Lincall.domain.Room;
import com.Capstone.Lincall.service.ConsultingService;
import com.Capstone.Lincall.socket.WebSocketMessageController;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @GetMapping("/records/client")
    @ResponseBody
    public List<ConsultingView> getClientConsultingRecord(String clientID){
        return consultingService.getConsultingsByClient(clientID);
    }

    @GetMapping("/records/counselor")
    @ResponseBody
    public List<ConsultingView> getCounselorConsultingRecord(String id){
        return consultingService.getConsultingsByCounselor(id);
    }

    @GetMapping("/room-list")
    @ResponseBody
    public List<Room> getRoomList(){
        Collections.sort(webSocketMessageController.roomList);
        return webSocketMessageController.roomList;
    }

    @GetMapping("/counselorInfo")
    @ResponseBody
    public String getCounselorInfo(String id){
        return consultingService.getCounselorInfo(id);
    }

    @GetMapping("/counselorInfo/today")
    @ResponseBody
    public String getCounselorInfoToday(String id){
        return consultingService.getCounselorInfoToday(id);
    }

    @GetMapping("/waitClient")
    @ResponseBody
    public int getCountOfWaitClient(){
        return webSocketMessageController.roomList.size();
    }

    @GetMapping("/wordcloud/happy")
    @ResponseBody
    public String getHappyWordCloudByConsultingID(String id){
        return consultingService.getHappyWordcloud(id);
    }

    @GetMapping("/wordcloud/angry")
    @ResponseBody
    public String getAngryWordCloudByConsultingID(String id){
        return consultingService.getAngryWordcloud(id);
    }


}
