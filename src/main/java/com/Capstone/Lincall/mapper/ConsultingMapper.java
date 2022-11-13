package com.Capstone.Lincall.mapper;

import com.Capstone.Lincall.domain.Consulting;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ConsultingMapper {
    @Insert("insert into consulting(counselor, client, start, end) values(#{counselor}, #{client}, #{start}, #{end});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Consulting consulting);

}
