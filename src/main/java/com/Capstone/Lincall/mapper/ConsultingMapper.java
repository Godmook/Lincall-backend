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

    @Update("UPDATE consulting SET start = #{start} WHERE id = #{id};")
    int updateStart(String id, long start);

    @Update("UPDATE consulting SET end = #{end} WHERE id = #{id};")
    int updateEnd(String id, long end);

    @Select("SELECT * FROM consulting WHERE client = #{id} ORDER BY start DESC;")
    List<Consulting> getByClient(String id);

    @Select("SELECT * FROM consulting WHERE counselor = #{id} ORDER BY start DESC LIMIT 10;")
    List<Consulting> getByCounselor(String id);

    @Select("SELECT IFNULL(SUM(end-start), 0) AS time FROM consulting WHERE counselor = #{counselor} AND date_format(from_unixtime(start/1000), '%Y-%c') = date_format(now(), '%Y-%c');")
    int getSumOfConsultingTimeMonth(String counselor);

    @Select("SELECT IFNULL(SUM(end-start), 0) AS time FROM consulting WHERE counselor = 'counselor1' AND date_format(from_unixtime(start/1000), '%Y-%c-%e') = date_format(now(), '%Y-%c-%e');")
    int getSumOfConsultingTimeDay(String counselor);

    @Select("SELECT COUNT(*) AS count FROM consulting WHERE counselor = #{id} AND date_format(from_unixtime(start/1000), '%Y-%c-%e') = date_format(now(), '%Y-%c-%e');")
    int getCountOfConsultingToday(String id);

    @Delete("DELETE FROM consulting WHERE id = #{id};")
    int removeConsulting(String id);
}
