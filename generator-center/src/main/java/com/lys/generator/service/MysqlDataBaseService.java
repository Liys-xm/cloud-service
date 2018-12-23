package com.lys.generator.service;

import com.lys.generator.entity.ColumnEntity;
import com.lys.generator.entity.TableEntity;
import com.lys.generator.mapper.SysGeneratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取mysql数据库信息
 */


@Service("mysqlDataBaseService")
public class MysqlDataBaseService extends DataBaseInfo {

    @Autowired
    private SysGeneratorMapper mapper;


    public String queryDatabaseName() {
        return mapper.queryDatabaseName();
    }

    public List<TableEntity> getTableList() {
        List<TableEntity> tables = mapper.queryTableList(null);


        tables.stream().forEach((tableInfo) -> {
            List<ColumnEntity> columns = mapper.selectAllColumns(tableInfo.getTableName());
            tableInfo.setColumns(columns);
        });
        return tables;
    }


}
