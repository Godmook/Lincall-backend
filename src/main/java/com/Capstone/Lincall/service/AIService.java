package com.Capstone.Lincall.service;


import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class AIService {

    // flask 연결 test
    public String flaskTest(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:5000")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        return result.getBody();
    }

    // 유사 질문 추천
    public String getSimilarQuestion(String str){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:5000/question")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject properties = new JSONObject();
        properties.put("question", str);

        HttpEntity<String> request = new HttpEntity<>(properties.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);

        return result.getBody();
    }
}
