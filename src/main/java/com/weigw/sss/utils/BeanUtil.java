package com.weigw.sss.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanUtil implements ApplicationContextAware  {
	
	private static ApplicationContext ac;
	public static Object getBean(String beanName){
		return ac.getBean(beanName);
	}
	public static <T> T getBean(Class<T> clazz){
		return ac.getBean(clazz);
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		BeanUtil.ac=applicationContext;
	}
}
