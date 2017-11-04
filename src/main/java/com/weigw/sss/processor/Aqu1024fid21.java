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

//电驴迅雷
public class Aqu1024fid21 extends ProcessorParse {

	private static final String FID = "21";
	private BaseService baseService;
	private ApplicationContext context;

	public Aqu1024fid21() {
		context = new ClassPathXmlApplicationContext("classpath:spring-base.xml");
	}
	
	// 列表页正则
	public String URL_LIST = "http://w2\\.aqu1024\\.net/pw/thread\\.php\\?fid=" + FID + "&page=\\d{1,3}|199";
//	public String URL_LIST = "http://w2\\.aqu1024\\.net/pw/thread\\.php\\?fid=" + FID + "&page=\\d[1-10]";
	
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
			String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
//			System.out.println("电驴迅雷 file_name h1--->" + field);
			
			String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
//			System.out.println(content);
			HashMap<String, Object> map;
			try {
				int pos1 = content.indexOf("】");
				int pos2 = content.indexOf("【", pos1);

				if(pos1>0&&pos2>0) {
					field = content.substring(pos1 + 2, pos2 - 4).replace("&nbsp;", "").trim();
				}
			
				// String temp3 = "<a" + " href=\"";
				// String temp4 = "target=\"_blank\"";
				// // 下载url
				// String btUrl = content.substring(content.lastIndexOf(temp3) + temp3.length(),
				// content.lastIndexOf(temp4) - 2);
				// 图片地址
				List<String> ImgUrl = new ArrayList<>();
				Pattern p = Pattern.compile("<img src=\".*\" border=\"0\"");
				Matcher m = p.matcher(content);
				while (m.find()) {
					String url = m.group();
					String lastUrl = url.substring(url.indexOf("<img") + 10, url.indexOf("border") - 2);
					ImgUrl.add(lastUrl);
				}

				String btUrl = "无";
				int btIndex = content.lastIndexOf("thunder");
				if(btIndex>0) {
					btUrl = content.substring(btIndex).replace("<br>", "");
				}
//				Pattern p2 = Pattern.compile("thunder.*");
//				Matcher m2 = p2.matcher(content);
//				while (m2.find()) {
//					String path = m2.group();
//					btUrl = path.substring(0, path.length() - 2);
//				}
//				System.out.println("bturl--->" + btUrl);
//				map = new HashMap<>();
				map = new HashMap<>();
				
				String snValue = "无";
				if (content.contains("商品番号")) {
					int snIndex = content.indexOf("商品番号");
					if(snIndex>0) {
						snValue = content.substring(snIndex + 6, content.indexOf("<br>", snIndex)).replace("&nbsp;",
								"");
					}
					 
				}

				String actorValue = "无";
				if (content.contains("影片主角")) {
					int actorIndex = content.indexOf("影片主角");
					
					 actorValue = content.substring(actorIndex + 6, content.indexOf("<br>", actorIndex))
							.replace("&nbsp;", "").replace("\n", "");
					
				}
				map.put("sn", snValue);
				map.put("actor", actorValue);
				map.put("film_name", field);
				map.put("bt_url", btUrl);
				map.put("img_url", StringUtils.collectionToDelimitedString(ImgUrl, ","));
				map.put("fid", FID);
				map.put("source_url", nowUrl);
				System.out.println("map--->" + map.toString());
				baseService = (BaseService) context.getBean("baseService");
				baseService.insertDB(map, "film");
			} catch (Exception e) {
				e.printStackTrace();
//				page.putField("exception", e.getMessage()); // 当前文章界面
//				System.out.println("电驴迅雷  Exception------------------------->" +page.getUrl());
			}
		}

	}

}
