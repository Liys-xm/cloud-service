package com.cloud.txtransaction.dao;

import com.cloud.txtransaction.entity.TxManager;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface TxManagerMapper {


    @Select("SELECT * FROM TX_MANAGER")
    List<TxManager> findAll();

    @Insert("INSERT INTO TX_MANAGER(NAME) VALUES(#{name})")
    int save(@Param("name") String name);

}
