package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/AI")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    AIService aiService;

    // flask 연동 test code
    @PostMapping("/flask-test")
    @ResponseBody
    public String test(){
        return aiService.flaskTest();
    }

    // 유사 질문 추천
    @PostMapping("/question")
    @ResponseBody
    public String similarQuestion(@RequestBody String question){
        return aiService.getSimilarQuestion(question);
    }
}
