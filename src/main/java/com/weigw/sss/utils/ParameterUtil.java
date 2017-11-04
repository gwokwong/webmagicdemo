package com.weigw.sss.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class ParameterUtil {
	public static Map<String,Object> parmeterRequest(HttpServletRequest request){
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		@SuppressWarnings("unchecked")
		Enumeration<String> pars=request.getParameterNames();
		while(pars.hasMoreElements()){
			String key=pars.nextElement();
			resultMap.put(key, request.getParameter(key));
		}
		if(resultMap.containsKey("page")&&resultMap.containsKey("rows")){
			Map<String,Object> pageMap=new HashMap<String,Object>();
			int page=Integer.parseInt(request.getParameter("page"));
			int rows=Integer.parseInt(request.getParameter("rows"));
			pageMap.put("ps", (page-1)*rows);
			pageMap.put("pe", rows);
			resultMap.put("page",pageMap);
		}
		if(resultMap.containsKey("sort")&&resultMap.containsKey("order")){
			Map<String,Object> sortMap=new HashMap<String,Object>();
			sortMap.put("sort", request.getParameter("sort"));
			sortMap.put("order", request.getParameter("order"));
			resultMap.put("sort",sortMap);
		}
		return resultMap;
	}
}
