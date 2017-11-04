package com.weigw.sss.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.weigw.sss.service.BaseService;

import us.codecraft.webmagic.Page;
//日本骑兵
public class Aqu1024fid22 extends ProcessorParse {

	private static final String FID = "22";
	private BaseService baseService;
	private ApplicationContext context;

	public Aqu1024fid22() {
		context = new ClassPathXmlApplicationContext("classpath:spring-base.xml");
	}

	// 列表页正则
	public String URL_LIST = "http://w2\\.aqu1024\\.net/pw/thread\\.php\\?fid=" + FID + "&page=\\d{1,3}|199";
	// 详情页正则
	public String URL_DETAIL1 = "htm_data/" + FID + "/\\d+/\\d+\\.html";
	
	public String URL_DETAIL2 = "read.php\\?tid=\\d+&fpage=\\d+";

	@Override
	public void parse(Page page) {
		if (page.getUrl().regex(URL_LIST).match()) { // 列表页
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\'t z\']").links().regex(URL_DETAIL1).all());
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\'t z\']").links().regex(URL_DETAIL2).all());
			List<String> htmlList = page.getHtml().links().regex(URL_LIST).all();
			page.addTargetRequests(htmlList);
		} else { // 文章页
			String nowUrl = page.getUrl().toString();
			// page.putField("currentUrl", page.getUrl()); // 当前文章界面
			String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
			// page.putField("field", field); // 这个就是名字
			System.out.println("日本骑兵file_name--->" + field);
			HashMap<String, Object> map;

			String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
			try {

				String temp3 = "<a" + " href=\"";
				String temp4 = "target=\"_blank\"";
				// 下载url
				String btUrl = content.substring(content.lastIndexOf(temp3) + temp3.length(),
						content.lastIndexOf(temp4) - 2);
				// 图片地址
				List<String> ImgUrl = new ArrayList<>();
				Pattern p = Pattern.compile("<img src=\".*\" border=\"0\"");
				Matcher m = p.matcher(content);
				while (m.find()) {
					String url = m.group();
					String lastUrl = url.substring(url.indexOf("<img") + 10, url.indexOf("border") - 2);
					ImgUrl.add(lastUrl);
				}

				map = new HashMap<>();
				map.put("film_name", field);
				map.put("bt_url", btUrl);
				map.put("img_url", StringUtils.collectionToDelimitedString(ImgUrl, ","));
				map.put("fid", FID);
				map.put("source_url", nowUrl);

				baseService = (BaseService) context.getBean("baseService");
				baseService.insertDB(map, "film");
			} catch (Exception e) {
				e.printStackTrace();
				page.putField("exception", e.getMessage()); // 当前文章界面
				System.out.println("日本骑兵 Exception------------------------->" + e.getMessage());
			}
		}

	}

}
