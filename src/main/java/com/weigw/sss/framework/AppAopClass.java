package com.weigw.sss.framework;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.weigw.sss.service.BaseService;
import com.weigw.sss.utils.ParameterUtil;

@Component
public class AppAopClass extends Thread {
	private Logger log = Logger.getLogger(AppAopClass.class);
	@Autowired
	private BaseService base;
	@Value("#{baseProperties['overtime']}")
	private int overtime = 0;

	public Object appRound(ProceedingJoinPoint point) throws Throwable {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("s", 1);
		resultMap.put("msg", null);
		resultMap.put("data", null);
		// resultMap.put("VisitTime", System.currentTimeMillis());
		//
		Object[] os = point.getArgs();
		HttpServletRequest request = null;
		if (os != null) {
			for (Object object : os) {
				if (object.getClass().getSimpleName().indexOf("Request") != -1) {
					request = (HttpServletRequest) object;
				}
			}
		}
		if (request == null) {
			resultMap.put("s", 2);
			resultMap.put("msg", "后台操作异常");
			log.error("请求方法必须包含request参数");
			return resultMap;
		} else {
			Map<String, Object> parMap = ParameterUtil.parmeterRequest(request);
			log.debug("app提交上来的参数:" + parMap);
			// // 开始登陆了
			// // 当用户名 密码 ssid openId都不存在时
			// if (username == null && password == null && openId == null
			// && token == null) {
			// resultMap.put("s", 3);
			// resultMap.put("msg", "无登陆信息");
			// return resultMap;
			// } else {
			//
			// String methodName = point.getSignature().getName();
			// if ("createUser".equals(methodName)
			// || "userLogin".equals(methodName)) {
			// Object returnValue = point.proceed();
			// @SuppressWarnings("unchecked")
			// Map<String, Object> resultMap1 = (Map<String, Object>) returnValue;
			// if (resultMap1.containsKey("s")) {
			// resultMap.put("s", resultMap1.get("s"));
			// resultMap1.remove("s");
			// }
			// if (resultMap1.containsKey("msg")) {
			// resultMap.put("msg", resultMap1.get("msg"));
			// resultMap1.remove("msg");
			// }
			// resultMap.put("data", resultMap1);
			// } else if (token != null) {
			//
		}

		return resultMap;
		// }
		// }
	}

	private Map<String, Object> invokeMethod(ProceedingJoinPoint point, Map<String, Object> resultMap) {
		try {
			Object resultValue = point.proceed();
			try {
				@SuppressWarnings("unchecked")
				Map<String, Object> tempMap = (Map<String, Object>) resultValue;
				if (tempMap.containsKey("s")) {
					resultMap.put("s", tempMap.get("s"));
				}
				if (tempMap.containsKey("msg")) {
					resultMap.put("msg", tempMap.get("msg"));
				}
				if (tempMap.containsKey("data")) {
					resultMap.put("data", tempMap.get("data"));
				} else {
					resultMap.put("data", resultValue);
				}
			} catch (Exception e) {
				resultMap.put("data", resultValue);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			resultMap.put("s", 2);
			resultMap.put("msg", "后台系统操作异常");
		}
		return resultMap;
	}

}
