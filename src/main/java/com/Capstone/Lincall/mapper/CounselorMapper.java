package com.Capstone.Lincall.mapper;

import com.Capstone.Lincall.domain.User;
import com.Capstone.Lincall.sql.ClientSqlProvider;
import com.Capstone.Lincall.sql.CounselorSqlProvider;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CounselorMapper {
    @InsertProvider(type = CounselorSqlProvider.class, method = "saveSql")
    int save(User user);

    @SelectProvider(type = CounselorSqlProvider.class, method = "findByID")
    User findByID(String id);

    @Update("UPDATE counselor SET password = #{nPW} WHERE id = #{id};")
    int updatePassword(String id, String nPW);

    @Delete("DELETE FROM counselor WHERE id = #{id};")
    void remove(String id);
}
