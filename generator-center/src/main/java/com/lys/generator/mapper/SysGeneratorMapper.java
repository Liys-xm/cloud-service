package com.lys.generator.mapper;

import com.lys.generator.entity.ColumnEntity;
import com.lys.generator.entity.ReferencedTable;
import com.lys.generator.entity.TableEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 日了狗-代码生成器
 */
@Mapper
public interface SysGeneratorMapper {

    List<Map<String, Object>> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);

    List<ReferencedTable> queryReferenced(String tableName);

    String queryDatabaseName();

    List<TableEntity> queryTableList(Map<String, Object> map);

    List<ColumnEntity> selectAllColumns(String tableName);

    List<TableEntity> queryOracleTableList(Map<String, Object> map);

    List<ColumnEntity> selectAllOracleColumns(String tableName);
}
