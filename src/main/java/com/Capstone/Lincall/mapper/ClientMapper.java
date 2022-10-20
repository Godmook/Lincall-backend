package com.Capstone.Lincall.mapper;

import com.Capstone.Lincall.domain.Client;
import com.Capstone.Lincall.sql.ClientSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Member;

@Mapper
public interface ClientMapper {
    @InsertProvider(type = ClientSqlProvider.class, method = "saveSql")
    int save(Client client) throws Exception;
}
