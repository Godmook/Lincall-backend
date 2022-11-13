package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.service.ConsultingService;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public void createNewConsulting(@RequestBody createModel model){
        consultingService.createConsulting(model.counselor, model.client);
    }

}
