package com.Capstone.Lincall.mapper;

import com.Capstone.Lincall.domain.Client;
import com.Capstone.Lincall.sql.ClientSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.sql.SQLIntegrityConstraintViolationException;

@Mapper
public interface ClientMapper {
    @InsertProvider(type = ClientSqlProvider.class, method = "saveSql")
    int save(Client client);

    @SelectProvider(type = ClientSqlProvider.class, method = "findByID")
    Client findByID(String id);
}
