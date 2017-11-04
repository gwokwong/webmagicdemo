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

//多挂原创
public class Aqu1024fid39 extends ProcessorParse {
	
	private static final String FID = "39";
	private BaseService baseService;
	private ApplicationContext context;

	public Aqu1024fid39() {
		context = new ClassPathXmlApplicationContext("classpath:spring-base.xml");
	}

	// 列表页正则
	public String URL_LIST = "http://w2\\.aqu1024\\.net/pw/thread\\.php\\?fid=" + FID + "&page=\\d{1,3}|100";
//	// 详情页正则
	public String URL_DETAIL1 = "htm_data/" + FID + "/\\d+/\\d+\\.html";
	
	//read.php?tid=\\d+&fpage=\\d+
	public String URL_DETAIL2 = "read.php\\?tid=\\d+&fpage=\\d+";

	@Override
	public void parse(Page page) {
		if (page.getUrl().regex(URL_LIST).match()) { // 列表页
			System.err.println("列表页--->"+page.getUrl());
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\'t z\']").links().regex(URL_DETAIL1).all());
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\'t z\']").links().regex(URL_DETAIL2).all());
			List<String> htmlList = page.getHtml().links().regex(URL_LIST).all();
			page.addTargetRequests(htmlList);
		} else { // 文章页
			
			String nowUrl = page.getUrl().toString();
			System.out.println("now url ---->" + nowUrl);
			page.putField("currentUrl", page.getUrl()); // 当前文章界面
			String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
			// page.putField("field", field);
			if (field.contains("經典推薦")) {
				String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
				String[] itemArr = content.split("<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n"
						+ "<br>﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n"
						+ "<br>\n" + "<br>");
				HashMap<String, Object> map;
				for (String str : itemArr) {
					// String temp1 = "影片名稱";
					// String temp2 = "影片格式";
					//
					// String temp11 = "影片名称";
					// String temp12 = "格式类型";
					//
					// String temp21 = "影片片名";
					// String temp22 = "影片大小";

					if (str.length() > 200) {
						try {
							String filmName = "";
							// if (str.contains(temp1)) {
							// filmName = str.substring(str.indexOf(temp1) + temp1.length() + 2,
							// str.indexOf(temp2) - 5).replace("&nbsp;", "").trim();
							// } else if (str.contains(temp11)) {
							// filmName = str.substring(str.indexOf(temp11) + temp11.length() + 2,
							// str.indexOf(temp12) - 5).replace("&nbsp;", "").trim();
							// } else if (str.contains(temp21)) { // 先弥补一部分数据
							// filmName = str.substring(str.indexOf(temp21) + temp21.length() + 2,
							// str.indexOf(temp22) - 5).replace("&nbsp;", "").trim();
							// }

							int pos1 = str.indexOf("】");
							int pos2 = str.indexOf("【", pos1);

							filmName = str.substring(pos1 + 2, pos2 - 4).replace("&nbsp;", "").trim();

							System.out.println("filmName------------------->" + filmName); // 电影名称
							String temp3 = "<a" + " href=\"";
							String temp4 = "/\" target=\"_blank\">";
							// 下载url
							String btUrl = str.substring(str.indexOf(temp3) + temp3.length(), str.indexOf(temp4));
							// 图片地址
							List<String> ImgUrl = new ArrayList<>();
							Pattern p = Pattern.compile("<img src=\".*\" border=\"0\"");
							Matcher m = p.matcher(str);
							while (m.find()) {
								String url = m.group();
								String lastUrl = url.substring(url.indexOf("<img") + 10, url.indexOf("border") - 2);
								ImgUrl.add(lastUrl);
							}

							map = new HashMap<>();
							map.put("film_name", filmName);
							map.put("bt_url", btUrl);
							map.put("img_url", StringUtils.collectionToDelimitedString(ImgUrl, ","));
							map.put("fid", FID);
							map.put("source_url", nowUrl);

							baseService = (BaseService) context.getBean("baseService");
							baseService.insertDB(map, "film");
						} catch (Exception e) {
							e.printStackTrace();
							page.putField("exception", e.getMessage()); // 当前文章界面
							System.out.println("Exception------------------------->" + e.getMessage());
						}
					}
				}
			}
		}

	}

}
