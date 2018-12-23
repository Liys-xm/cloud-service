package com.lys.generator.service;

import com.lys.generator.entity.ColumnEntity;
import com.lys.generator.entity.TableEntity;
import com.lys.generator.mapper.SysGeneratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取oracle数据库信息
 */


@Service("oracleDataBaseService")
public class OracleDataBaseService extends DataBaseInfo {

    @Autowired
    private SysGeneratorMapper mapper;

    @Value("${spring.datasource.username}")
    private String databaseName;


    @Override
    public String queryDatabaseName() {
        // TODO Auto-generated method stub
        return databaseName;
    }

    @Override
    public List<TableEntity> getTableList() {
        List<TableEntity> tables = mapper.queryOracleTableList(null);


        tables.stream().forEach((tableInfo) -> {
            List<ColumnEntity> columns = mapper.selectAllOracleColumns(tableInfo.getTableName());
            tableInfo.setColumns(columns);
        });
        return tables;
    }

}
