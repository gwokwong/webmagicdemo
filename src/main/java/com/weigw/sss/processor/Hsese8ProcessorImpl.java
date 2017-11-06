package com.weigw.sss.processor;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.weigw.sss.service.BaseService;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

// 美女写真福利瑟舍
public class Hsese8ProcessorImpl implements PageProcessor {

	protected BaseService bs;
	protected ApplicationContext context;
	private static String beginUrl = "http://hsese8.over-blog.com/page/1";
	protected String URL_LIST = "http://hsese8.over-blog.com/page/\\d+";

	private Site site = Site.me().setSleepTime(1000).setUserAgent("chrome");
	// String imgRegex =
	// "http://mm.howkuai.com/wp-content/uploads/20[0-9]{2}[a-z]/[0-9]{1,4}/[0-9]{1,4}/[0-9]{1,4}.jpg";

	String imgRegex = "https://img\\.over-blog-kiwi\\.com/.*\\.JPG";

	public Hsese8ProcessorImpl() {
		context = new ClassPathXmlApplicationContext("classpath:spring-base.xml");
		bs = (BaseService) context.getBean("baseService");
	}

	@Override
	public void process(Page page) {
		// System.err.println("now url------------------>" + page.getUrl());
		List<String> htmlList = page.getHtml().links().regex(URL_LIST).all();
		page.addTargetRequests(htmlList);
		// List<Selectable> nodes =
		// page.getHtml().xpath("//div[@class='slideshow']/article/div[contains(@class,'tagline')]").nodes();
		List<Selectable> nodes = page.getHtml().xpath("//div[@class='slideshow']/article").nodes();
		// System.err.println("nodes size -------------->" + nodes.size());
		HashMap<String, Object> map;
		try {
			for (Selectable node : nodes) {
				// System.err.println(" node-->" + node.toString());
				map = new HashMap<>();
				List<String> listProcess = node.regex(imgRegex).all();
				map.put("img_url", StringUtils.collectionToDelimitedString(listProcess, ","));
				map.put("film_name", StringUtils.collectionToDelimitedString(listProcess, ","));
				System.err.println("map-->" + map.toString());
				// bs.insertDB(map, "film");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new Hsese8ProcessorImpl()).addUrl(beginUrl).thread(5).run();
	}
}
