package com.weigw.sss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weigw.sss.dao.BaseDao;

@Service
public class BaseService {
	public static Map<String, List<Map<String, Object>>> dbCache = null;
	@Autowired
	private BaseDao dao;

	public int insertDB(Map<String, Object> data, String tableName) {
//		data.put("c_time", new Date());
//		data.put("c_user_id", UserUtil.getLoginUserId());
//		data.put("c_user", UserUtil.getLoginUserName());
		tableName = tableName.toLowerCase();
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("tableName", tableName);
		parMap.put("data", getData(data, tableName));
		dao.insertDB(parMap);
		return parMap.containsKey("id") ? Integer.parseInt(parMap.get("id")
				.toString()) : -1;
	}

	private static List<Map<String, Object>> getData(Map<String, Object> data,
			String tableName) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		tableName = tableName.toLowerCase();
		List<Map<String, Object>> tempList = dbCache.get(tableName);
		for (Map<String, Object> map : tempList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String colName = map.get("COLUMN_NAME").toString();
			Object value = data.get(colName);
			tempMap.put("col", colName);
			tempMap.put("val", value);
			if (value != null) {
				resultList.add(tempMap);
			}
		}
		return resultList;
	}

	private static List<Map<String, Object>> getDataWithNull(
			Map<String, Object> data, String tableName) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		tableName = tableName.toLowerCase();
		List<Map<String, Object>> tempList = dbCache.get(tableName);
		for (Map<String, Object> map : tempList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String colName = map.get("COLUMN_NAME").toString();
			Object value = data.get(colName);
			tempMap.put("col", colName);
			tempMap.put("val", value);
			resultList.add(tempMap);
		}
		return resultList;
	}

	public int updateDB(Map<String, Object> data, String tableName) {
		tableName = tableName.toLowerCase();
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("tableName", tableName);
		parMap.put("data", getData(data, tableName));
		parMap.put("id", data.get("id"));
		return dao.updateDb(parMap);
	}

	public int deleteDb(Map<String, Object> data, String tableName) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("tableName", tableName);
		parMap.put("data", data);
		return dao.deleteDb(parMap);
	}

	public List<Map<String, Object>> simpleSelect(Map<String, Object> keys,
			String tableName) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		List<Map<String, Object>> keyList = new ArrayList<Map<String, Object>>();
		Set<String> keyys = keys.keySet();
		for (String string : keyys) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("key", string);
			tempMap.put("value", keys.get(string));
			keyList.add(tempMap);
		}
		parMap.put("keyList", keyList);
		parMap.put("tableName", tableName);
		return dao.simpleSelect(parMap);
	}
	
	
	public List<Map<String, Object>> simpleSelectTableLike(Map<String, Object> keys,
			String tableName) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		List<Map<String, Object>> keyList = new ArrayList<Map<String, Object>>();
		Set<String> keyys = keys.keySet();
		for (String string : keyys) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("key", string);
			tempMap.put("value", keys.get(string));
			keyList.add(tempMap);
		}
		parMap.put("keyList", keyList);
		parMap.put("tableName", tableName);
		return dao.simpleSelectTableLike(parMap);
	}
	
	/**新增多个or条件一起的查询**/
	public List<Map<String, Object>> simpleSelectOr(Map<String, Object> keys,
			String tableName) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		List<Map<String, Object>> keyList = new ArrayList<Map<String, Object>>();
		Set<String> keyys = keys.keySet();
		for (String string : keyys) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("key", string);
			tempMap.put("value", keys.get(string));
			keyList.add(tempMap);
		}
		parMap.put("keyList", keyList);
		parMap.put("tableName", tableName);
		return dao.simpleSelect(parMap);
	}

	public List<Map<String, Object>> simpleSelect(String tableName) {
		return dao.simpleSelectTable(tableName);
	}

	public boolean simpleCheckData(Map<String, Object> keys, String tableName) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		List<Map<String, Object>> keyList = new ArrayList<Map<String, Object>>();
		parMap.put("keyList", keyList);
		Set<String> keyys = keys.keySet();
		for (String string : keyys) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("key", string);
			tempMap.put("value", keys.get(string));
			keyList.add(tempMap);
		}
		parMap.put("tableName", tableName);
		return dao.simpleCheckData(parMap) > 0;
	}

	public int isnertBatch(List<Map<String, Object>> data, String tableName) {
		if (data == null || data.size() == 0) {
			return 0;
		}
		List<List<Map<String, Object>>> targetList = new ArrayList<List<Map<String, Object>>>();
		for (Map<String, Object> map : data) {
			targetList.add(getDataWithNull(map, tableName));
		}
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("tableName", tableName);
		parMap.put("data", targetList);
		parMap.put("col", dbCache.get(tableName));
		return dao.insertBatch(parMap);
	}

	public int deleteBatch(List<Map<String, Object>> data, String tableName) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("data", data);
		parMap.put("tableName", tableName);
		return dao.deleteBatch(parMap);
	}
	
	
	/**
	 * 
	 * @param data
	 * @param tableName
	 * @param columName
	 * @return
	 * @since WeiGuoWang
	 */
	public List<Map<String, Object>> simpleSelectTableIn(List<Map<String, Object>> data, String tableName,String columName) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("data", data);
		parMap.put("columName", columName);
		parMap.put("tableName", tableName);
		return dao.simpleSelectTableIn(parMap);
	}

	@Autowired
	private void getDbInfo() {
		Map<String, Object> parMap = new HashMap<String, Object>();
		String dbName = null;
		dbName = dao.getDbName();
		parMap.put("dbName", dbName);
		dbCache = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> tableInfoList = dao.getTableInfo(parMap);
		for (Map<String, Object> map : tableInfoList) {
			String tableName = map.get("TABLE_NAME").toString().toLowerCase();
			if (!dbCache.containsKey(tableName)) {
				dbCache.put(tableName, new ArrayList<Map<String, Object>>());
			}
			dbCache.get(tableName).add(map);
		}
	}
}
