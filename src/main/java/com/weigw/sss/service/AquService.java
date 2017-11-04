package com.weigw.sss.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AquService {
	@Autowired
	private BaseService bs;

	
	public Object webMagic(Map<String, Object> parMap) {
		List<Map<String, Object>> tempList = null;
		parMap.put("status", "success");
		return tempList;
	}
	
	
	
	
	
	
}

