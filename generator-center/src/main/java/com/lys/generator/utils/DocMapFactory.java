package com.lys.generator.utils;


import com.lys.generator.entity.TableEntity;
import com.lys.generator.service.MysqlDataBaseService;
import com.lys.generator.service.OracleDataBaseService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DocMapFactory {

    public static Map build() throws SQLException {
        Map map = new HashMap();
        DataSource dataSource = (DataSource) com.lys.generator.utils.SpringContextUtils.getBean("dataSource");
        String databaseProductName = dataSource.getConnection().getMetaData().getDatabaseProductName();
        if (("MySQL").equals(databaseProductName)) {

            MysqlDataBaseService mysqlDataBaseService = (MysqlDataBaseService) com.lys.generator.utils.SpringContextUtils.getBean("mysqlDataBaseService");
            String databaseName = mysqlDataBaseService.queryDatabaseName();
            List<TableEntity> tableInfos = mysqlDataBaseService.getTableList();

            map.put("tableInfos", tableInfos);
            map.put("databaseName", databaseName);


        } else if (("Oracle").equals(databaseProductName)) {

            OracleDataBaseService oracleDataBaseService = (OracleDataBaseService) com.lys.generator.utils.SpringContextUtils.getBean("oracleDataBaseService");
            String databaseName = oracleDataBaseService.queryDatabaseName();
            List<TableEntity> tableInfos = oracleDataBaseService.getTableList();

            map.put("tableInfos", tableInfos);
            map.put("databaseName", databaseName);


        }

        return map;
    }
}
