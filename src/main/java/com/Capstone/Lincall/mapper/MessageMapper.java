package com.Capstone.Lincall.mapper;

import com.Capstone.Lincall.domain.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
}
