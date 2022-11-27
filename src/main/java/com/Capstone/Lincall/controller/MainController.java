package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.domain.AngerPoint;
import com.Capstone.Lincall.domain.Message;
import com.Capstone.Lincall.service.MainService;
import com.Capstone.Lincall.socket.WebSocketMessageController;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value="/main")
@CrossOrigin(origins = "*")
public class MainController {
    @Autowired MainService mainService;

    @Getter
    static class AddTextModel{
        int roomId;
        String from;
        long time;
        String encodeStr;
    }
    @PostMapping("/addText")
    @ResponseBody
    public void addText(@RequestBody AddTextModel model) throws Exception {
        mainService.addText(model.roomId, model.from, model.time, model.encodeStr);
    }

    @GetMapping("/dialogue")
    @ResponseBody
    public void getDialogue(int roomId){
        mainService.getDialogue(roomId);
    }

    @GetMapping("/angerPoint")
    @ResponseBody
    public List<AngerPoint> getAngerPoints(int roomId){
        return mainService.getAngerPoints(roomId);
    }

    @GetMapping("/todayKeyword/happy")
    @ResponseBody
    public List<String> getTodayHappyKeyword(){
        return mainService.getTodayHappyKeyword();
    }

    @GetMapping("/todayKeyword/angry")
    @ResponseBody
    public List<String> getTodayAngryKeyword(){
        return mainService.getTodayAngryKeyword();
    }
}
