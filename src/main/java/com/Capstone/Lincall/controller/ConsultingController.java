package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.domain.ConsultingView;
import com.Capstone.Lincall.service.ConsultingService;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value="/consulting")
@CrossOrigin(origins = "*")
public class ConsultingController {
    ConsultingService consultingService;

    public ConsultingController(ConsultingService consultingService){
        this.consultingService = consultingService;
    }

    @Getter
    static class createModel{
        private String counselor;
        private String client;
    }
    @PostMapping("/create")
    @ResponseBody
    public int createNewConsulting(@RequestBody createModel model){
        return consultingService.createConsulting(model.counselor, model.client);
    }

    @GetMapping("/end")
    @ResponseBody
    public void endConsulting(String id){
        consultingService.endConsulting(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ConsultingView> getConsultingList(String clientID){
        return consultingService.getConsultingsByClient(clientID);
    }

}
