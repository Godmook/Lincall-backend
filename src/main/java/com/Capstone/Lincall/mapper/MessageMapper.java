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
    public int insertMessage(String roomId, String type, long time, String emotion, String text, String keyword);

    @Select("SELECT type, time, emotion, text FROM message WHERE roomID = #{roomId} ORDER BY time ASC;")
    public List<Message> getByRoomId(int roomId);
}
