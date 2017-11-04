package com.weigw.sss.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao {

	String getDbName();

	List<Map<String, Object>> getTableInfo(Map<String, Object> parMap);

	int insertDB(Map<String, Object> parMap);

	int updateDb(Map<String, Object> parMap);

	int deleteDb(Map<String, Object> parMap);

	List<Map<String, Object>> simpleSelect(Map<String, Object> parMap);
	
	List<Map<String, Object>> simpleSelectTableIn(Map<String, Object> parMap);
	
	List<Map<String, Object>> simpleSelectTableLike(Map<String, Object> parMap);

	int simpleCheckData(Map<String, Object> parMap);

	List<Map<String, Object>> simpleSelectTable(String tableName);

	int insertBatch(Map<String, Object> parMap);

	int deleteBatch(Map<String, Object> parMap);
	

}
