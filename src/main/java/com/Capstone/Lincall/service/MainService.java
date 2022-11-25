package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.AngerPoint;
import com.Capstone.Lincall.domain.Message;
import com.Capstone.Lincall.mapper.MessageMapper;
import com.Capstone.Lincall.socket.WebSocketMessageController;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {
    @Autowired VitoService vitoService;
    @Autowired MessageMapper messageMapper;

    @Autowired WebSocketMessageController websocket;

    public void addText(int roomId, String userType, long time, String encodeStr ) throws Exception {
        byte[] decodeBytes = Base64.decodeBase64(encodeStr);
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

        JSONArray arr = obj.getJSONObject("results").getJSONArray("utterances");
        String message = "";
        for(int j=0; j<arr.length(); j++){
            JSONObject text = arr.getJSONObject(j);
            message += " " + text.getString("msg");
        }

        // emotion
        String emotion = "angry"; //test

        // 질문 추천
        String question = null; //test
        String answer = null;

        // 추가할 text 생성
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

        if(userType.equals("counselor")) return;

        // client인 경우만 확인

        // keyword (감정 변화가 생긴 경우)
        String keyword = null;
        boolean isStartingPoint = false;
        String pastEmotion = messageMapper.getLastEmotion(roomId, userType);
        if(pastEmotion == null || pastEmotion != "angry"){
            isStartingPoint = true;
            keyword = "키워드!"; //test
        }

        // db update
        messageMapper.insertMessage(roomId, userType, time, emotion, message, keyword);

        // anger starting point 업데이트 알림
        if(isStartingPoint)
            websocket.sendMessage(roomId, "reload anger starting point");

        // 음성 차단 활성화 여부 확인
        int angerCnt = messageMapper.getAngerCnt(roomId);
        if(angerCnt == 4) // 음성 차단 기준 = 4회
            websocket.sendMessage(roomId, "activate voice blocking");
    }

    public List<Message> getDialogue(int roomId) {
        return messageMapper.getByRoomId(roomId);
    }

    public List<AngerPoint> getAngerPoints(int roomId){
        return messageMapper.getAngerStartMessage(roomId);
    }

}
