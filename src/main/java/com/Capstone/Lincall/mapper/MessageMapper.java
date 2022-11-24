package com.Capstone.Lincall.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MessageMapper {
    @Insert("INSERT INTO  message(roomId, type, time, emotion, text, keyword) VALUES(#{roomId}, #{type}, #{time}, #{emotion}, #{text}, #{keyword})")
    public int updateDialogue(String roomId, String type, long time, String emotion, String text, String keyword);
}
