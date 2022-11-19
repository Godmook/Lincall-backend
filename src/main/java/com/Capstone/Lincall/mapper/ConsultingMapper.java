package com.Capstone.Lincall.mapper;

import com.Capstone.Lincall.domain.Consulting;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConsultingMapper {
    @Insert("insert into consulting(counselor, client, start, end) values(#{counselor}, #{client}, #{start}, #{end});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Consulting consulting);

    @Update("UPDATE consulting SET counselor = #{counselor}, client = #{client} where id = #{id};")
    int updateUserInfo(int id, String counselor, String client);

    @Update("UPDATE consulting SET end = #{end} WHERE id = #{id};")
    int updateEnd(String id, long end);

    @Select("select * from consulting where client = #{id} ORDER BY start desc;")
    List<Consulting> getByClient(String id);

}
