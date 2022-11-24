package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.service.MainService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="/main")
@CrossOrigin(origins = "*")
public class MainController {
    @Autowired MainService mainService;
    @Getter
    static class AddTextModel{
        String roomId;
        String from;
        long time;
        String encodeStr;
    }
    @PostMapping("/addText")
    @ResponseBody
    public String addText(@RequestBody AddTextModel model) throws Exception {
        return mainService.addText(model.roomId, model.from, model.time, model.encodeStr);
    }

}
