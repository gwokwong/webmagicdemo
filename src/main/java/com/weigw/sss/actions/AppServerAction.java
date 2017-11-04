package com.weigw.sss.actions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weigw.sss.service.AquService;
import com.weigw.sss.service.BaseService;
import com.weigw.sss.utils.ParameterUtil;

@Controller
@RequestMapping("/appServer")
public class AppServerAction {
	@Autowired
	private BaseService base;
	@Autowired
	private AquService aqu;

	@RequestMapping("/testAction")
	@ResponseBody
	public Object testAction(HttpServletRequest request) {		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> rMap= ParameterUtil.parmeterRequest(request);
		System.out.println("rmap------------------------->"+rMap);
		return base.simpleSelectTableLike(rMap, "film");
	}

	@RequestMapping("/doNow")
	@ResponseBody
	public Object doNow(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "sdfsa");
		return map;
	}

}
