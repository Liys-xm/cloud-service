package com.lys.generator.service;


import com.lys.generator.entity.TableEntity;

import java.util.List;


/** 
* 获取数据库信息
*/
public abstract class DataBaseInfo {

	/****
	 * 获取数据库名称
	 * 
	 * @return
	 */
	public abstract String queryDatabaseName();

	/***
	 * 获取表的信息
	 * 
	 * @return
	 */
	public  abstract List<TableEntity> getTableList();

}
