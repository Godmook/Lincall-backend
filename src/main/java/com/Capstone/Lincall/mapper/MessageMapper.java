package com.Capstone.Lincall.mapper;

import com.Capstone.Lincall.domain.AngerPoint;
import com.Capstone.Lincall.domain.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("INSERT INTO  message(roomId, type, time, emotion, text, keyword) VALUES(#{roomId}, #{type}, #{time}, #{emotion}, #{text}, #{keyword})")
    public int insertMessage(int roomId, String type, long time, String emotion, String text, String keyword);

    @Select("SELECT type, time, emotion, text FROM message WHERE roomID = #{roomId} ORDER BY time ASC;")
    public List<Message> getByRoomId(int roomId);

    @Select("SELECT emotion FROM message WHERE roomID = #{roomId} AND type = #{userType} ORDER BY time DESC LIMIT 1;")
    public String getLastEmotion(int roomId, String userType);

    @Select("SELECT COUNT(*) FROM message WHERE roomID = #{roomId} AND emotion = 'angry';")
    public int getAngerCnt(int roomId);

    @Select("SELECT text, time, keyword FROM message WHERE roomId = #{roomId} AND keyword IS NOT NULL;")
    public List<AngerPoint> getAngerStartMessage(int roomId);

    @Select("SELECT GROUP_CONCAT(text)  FROM message WHERE date_format(from_unixtime(time/1000), '%Y-%c-%e') = date_format(now(), '%Y-%c-%e') AND emotion = 'happy';")
    public String getTodayHappyMessage();

    @Select("SELECT GROUP_CONCAT(text)  FROM message WHERE date_format(from_unixtime(time/1000), '%Y-%c-%e') = date_format(now(), '%Y-%c-%e') AND emotion = 'angry';")
    public String getTodayAngryMessage();

    @Select("SELECT GROUP_CONCAT(text)  FROM message WHERE roomId = #{roomId} AND emotion = 'happy';")
    public String getHappyTextByRoomId(String roomId);

    @Select("SELECT GROUP_CONCAT(text)  FROM message WHERE roomId = #{roomId} AND emotion = 'angry';")
    public String getAngryTextByRoomId(String roomId);
}
