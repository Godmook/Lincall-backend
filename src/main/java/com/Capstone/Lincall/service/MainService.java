package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.AngerPoint;
import com.Capstone.Lincall.domain.Message;
import com.Capstone.Lincall.mapper.MessageMapper;
import com.Capstone.Lincall.socket.WebSocketMessageController;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class MainService {
    @Autowired VitoService vitoService;
    @Autowired MessageMapper messageMapper;

    @Autowired WebSocketMessageController websocket;

    private String baseUrl;

    MainService(){
        ResourceBundle rb = ResourceBundle.getBundle("flaskServer", Locale.KOREA);
        baseUrl = rb.getString("flaskURL");
    }

    public void addText(int roomId, String userType, long time, String encodeStr ) throws Exception {
        byte[] decodeBytes = Base64.decodeBase64(encodeStr.split(",")[1]);
        Path filePath = Paths.get("../voice/room" + roomId + "_" + userType + ".wav");
        Files.write(filePath, decodeBytes);

        String id = vitoService.requestTranscribe(filePath.toString());

        String response;
        JSONObject obj;
        // colplete 대기
        while(true){
            response = vitoService.getTranscribe(id);
            obj = new JSONObject(response);
            if(obj.keySet().contains("code")) continue;
            String status = obj.getString("status");
            if(status.equals("completed"))
                break;
            Thread.sleep(100);
        }

        // stt 결과 처리
        JSONArray arr = obj.getJSONObject("results").getJSONArray("utterances");
        String message = "";
        if(arr.length() == 0) return; // 음성 데이터 없는 경우

        for(int j=0; j<arr.length(); j++){
            JSONObject text = arr.getJSONObject(j);
            message += " " + text.getString("msg");
        }

        // flask 통신 세팅
        RestTemplate restTemplate = new RestTemplate();

        // emotion
        Double emotionDouble = Double.parseDouble(restTemplate.getForObject(baseUrl + "/sentiment?sentence="+message, String.class));
        String emotion = "none";
        if( emotionDouble< 0.1)
            emotion = "angry";
        else if(emotionDouble > 0.5)
                emotion = "happy";


        // 질문 추천  : flask 미구현
        String questionResponse = restTemplate.getForObject(baseUrl + "/question?sentence="+ message, String.class);
        String question = null;
        String answer = null;
        if(questionResponse != null){
            String[] questionAndAnwer = questionResponse.split("\\|");
            question = questionAndAnwer[0];
            answer = questionAndAnwer[1];
        }


        // fron 전달 객체
        JSONObject nText = new JSONObject();
        nText.put("type", userType);
        nText.put("message", message);
        nText.put("time", time);
        nText.put("emotion", emotion);
        // question, answer 없는 경우 포함 X
        nText.put("question", question);
        nText.put("answer", answer);

        // websocket 전달
        websocket.sendMessage(roomId, nText.toString());


        // client인 경우만 확인
        JSONArray keywordArr = null;
        String[] keyword = null;
        boolean isStartingPoint = false;
        if(userType.equals("client")){
            // keyword (감정 변화가 생긴 경우)
            String pastEmotion = messageMapper.getLastEmotion(roomId, userType);
            if(emotion.equals("angry") && (pastEmotion == null || pastEmotion != "angry")){
                isStartingPoint = true;
                keyword = restTemplate.getForObject(baseUrl + "/keyword?sentence="+ message, String.class).split(" ");
                keywordArr = new JSONArray();
                for(String s : keyword)
                    keywordArr.put(s);
            }
        }

        // db update
        messageMapper.insertMessage(roomId, userType, time, emotion, message, (keywordArr == null)? null :keywordArr.toString());

        // anger starting point 업데이트 알림
        if(isStartingPoint)
            websocket.sendMessage(roomId, "reload anger starting point");

        if(userType.equals("client") && emotion.equals("angry")){
            // 음성 차단 활성화 여부 확인
            int angerCnt = messageMapper.getAngerCnt(roomId);
            if(angerCnt == 4) // 음성 차단 기준 = 4회
                websocket.sendMessage(roomId, "activate voice blocking");
        }
    }

    public List<Message> getDialogue(int roomId) {
        return messageMapper.getByRoomId(roomId);
    }

    public List<AngerPoint> getAngerPoints(int roomId){
        return messageMapper.getAngerStartMessage(roomId);
    }

    public String getTodayHappyKeyword(){
        String todayHappyStr = messageMapper.getTodayHappyMessage();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(baseUrl + "/wordcloud?sentence="+todayHappyStr+"&theme=dark", String.class);
    }

    public String getTodayAngryKeyword(){
        String todayAngryStr = messageMapper.getTodayAngryMessage();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(baseUrl + "/wordcloud?sentence="+todayAngryStr+"&theme=dark", String.class);
    }


}
