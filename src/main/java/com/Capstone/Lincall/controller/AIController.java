package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/AI")
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
