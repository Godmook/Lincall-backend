package com.Capstone.Lincall.service;


import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class AIService {

    // flask 연결 test
    public String flaskTest(){
        RestTemplate restTemplate = new RestTemplate();
        ResourceBundle rb = ResourceBundle.getBundle("flaskServer", Locale.KOREA);
        String baseUrl = rb.getString("flaskURL");
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-32")));
        String questionResponse = restTemplate.getForObject(baseUrl + "/keyword?sentence="+ "주문하고 한시간 지났는데 안와요.  ", String.class);
        System.out.println(questionResponse);
        return questionResponse;
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
