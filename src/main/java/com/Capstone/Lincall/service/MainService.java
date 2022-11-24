package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.Message;
import com.Capstone.Lincall.mapper.MessageMapper;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MainService {
    @Autowired VitoService vitoService;
    @Autowired
    MessageMapper messageMapper;

    public String addText(String roomId, String userType, long time, String encodeStr ) throws Exception {
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

        // keyword (감정 변화가 생긴 경우)
        String keyword = null;  //test

        // 질문 추천
        String question = null; //test
        String answer = null;

        // 추가할 text 생성
        JSONObject nText = new JSONObject();
        nText.put("type", userType);
        nText.put("message", message);
        nText.put("time", time);
        nText.put("emotion", emotion);
        nText.put("question", question);
        nText.put("answer", answer);

        // db update
        messageMapper.insertMessage(roomId, userType, time, emotion, message, keyword);

        return nText.toString();
    }

    public List<Message> getDialogue(int roomId){
        return messageMapper.getByRoomId(roomId);
    }
}
