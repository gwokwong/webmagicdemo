package com.weigw.sss.processor;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.weigw.sss.service.BaseService;

import us.codecraft.webmagic.Page;

public abstract class AbsAqu1024 extends ProcessorParse {

	protected BaseService bs;

	protected ApplicationContext context;

	protected String URL_LIST = "http://w2\\.aqu1024\\.net/pw/thread\\.php\\?fid=" + getFid() + "&page=\\d{1,3}|999";

	public String URL_DETAIL1 = "htm_data/" + getFid() + "/\\d+/\\d+\\.html";

	public String URL_DETAIL2 = "read.php\\?tid=\\d+&fpage=\\d+";

	public AbsAqu1024() {
		context = new ClassPathXmlApplicationContext("classpath:spring-base.xml");
		bs = (BaseService) context.getBean("baseService");
	}

	public abstract int getFid();

	@Override
	public void parse(Page page) {
		if (page.getUrl().regex(URL_LIST).match()) { // 列表页
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\'t z\']").links().regex(URL_DETAIL1).all());
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\'t z\']").links().regex(URL_DETAIL2).all());
			List<String> htmlList = page.getHtml().links().regex(URL_LIST).all();
			page.addTargetRequests(htmlList);
		} else { // 文章页
			doDetailParse(page);
		}
	}

	public abstract void doDetailParse(Page page);

}
