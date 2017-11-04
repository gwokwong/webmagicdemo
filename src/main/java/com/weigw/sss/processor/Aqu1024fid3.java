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
//最新合集
public class Aqu1024fid3 extends ProcessorParse {

	private static final String FID = "3";
	private BaseService baseService;
	private ApplicationContext context;

	public Aqu1024fid3() {
		context = new ClassPathXmlApplicationContext("classpath:spring-base.xml");
	}

	// 列表页正则
	public String URL_LIST = "http://w2\\.aqu1024\\.net/pw/thread\\.php\\?fid=" + FID + "&page=\\d{1,3}|199";
	// 详情页正则
	public String URL_DETAIL1 = "htm_data/" + FID + "/\\d+/\\d+\\.html";
	
	public String URL_DETAIL2 = "read.php\\?tid=\\d+&fpage=\\d+";

	@Override
	public void parse(Page page) {
		System.out.println("进入aqu1024fid=3");
		if (page.getUrl().regex(URL_LIST).match()) { // 列表页
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\'t z\']").links().regex(URL_DETAIL1).all());
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\'t z\']").links().regex(URL_DETAIL2).all());
			List<String> htmlList = page.getHtml().links().regex(URL_LIST).all();
			page.addTargetRequests(htmlList);
		} else { // 文章页
			String nowUrl = page.getUrl().toString();
			page.putField("currentUrl", page.getUrl()); // 当前文章界面
			String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
			page.putField("field", field); // 当前正文
			if (field.contains("國產自拍")) {
				String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
				String[] itemArr = null;
				try {
					itemArr = content.replaceAll("\\[", "------").split("<br>\n" + "<br>\n" + "<br>\n" + "<br>------");
					// System.out.println("itemArr size -------->" + itemArr.length);
				} catch (Exception e) {
					e.printStackTrace();
				}

				HashMap<String, Object> map;
				for (String str : itemArr) {
					String temp21 = "影片名称";
					String temp22 = "影片格式";
					if (str.length() > 200) {
						try {
							String filmName = "";
							if (str.contains(temp21)) {
								filmName = str
										.substring(str.indexOf(temp21) + temp21.length() + 2, str.indexOf(temp22) - 5)
										.replace("&nbsp;", "").trim();
								System.out.println("filmName------------------->" + filmName); // 电影名称
								String temp3 = "<a" + " href=\"";
								String temp4 = "target=\"_blank\"";
								System.out.println("str.lastIndexOf(temp3)----->" + str.lastIndexOf(temp3));
								System.out.println("str.lastIndexOf(temp4)----->" + str.lastIndexOf(temp4));
								String btUrl = str.substring(str.lastIndexOf(temp3) + temp3.length(),
										str.lastIndexOf(temp4) - 2);
								// 图片地址
								List<String> ImgUrl = new ArrayList<>();
								// String temp5 = "<img src=\\\"";
								// String temp6 = "\"border=\"0";
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
								map.put("sn", "无");
								map.put("actor", "无");
								baseService = (BaseService) context.getBean("baseService");
								baseService.insertDB(map, "film");
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("currentUrl------------------->" + page.getUrl());
							System.out.println("str------------------->" + str.substring(0, 400));
						}
					}
				}
			} else if (field.contains("国产高清")) {
				String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
				// System.out.println("国产高清------>"+content);
				String[] itemArr = content.split("<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n"
						+ "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>");
				HashMap<String, Object> map;
				for (String str : itemArr) {
					if (str.length() > 200) {
						try {
							int pos1 = str.indexOf("】");
							int pos2 = str.indexOf("【", pos1);

							String filmName = str.substring(pos1 + 2, pos2 - 4).replace("&nbsp;", "").trim();

							System.out.println("国产高清filmName------------------->" + filmName); // 电影名称
							// String temp3 = "<a" + " href=\"";
							// String temp4 = "/\" target=\"_blank\">";
							String temp3 = "<a" + " href=\"";
							String temp4 = "target=\"_blank\"";
							// 下载url
							// String btUrl = str.substring(str.lastIndexOf(temp3) + temp3.length(),
							// str.lastIndexOf(temp4);
							String btUrl = str.substring(str.lastIndexOf(temp3) + temp3.length(),
									str.lastIndexOf(temp4) - 2);
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
							map.put("sn", "无");
							map.put("actor", "无");

							baseService = (BaseService) context.getBean("baseService");
							baseService.insertDB(map, "film");
						} catch (Exception e) {
							e.printStackTrace();
							page.putField("exception", e.getMessage()); // 当前文章界面
							System.out.println("Exception------------------------->" + e.getMessage());
						}
					}
				}
			} else if (field.contains("国产無碼")
			// ||field.contains("欧美無碼")
			) { // 和国产高清好像是一样的
				String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
				// System.out.println("国产高清------>"+content);
				String[] itemArr = content.split("<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>");
				HashMap<String, Object> map;
				for (String str : itemArr) {
					if (str.length() > 200) {
						try {
							int pos1 = str.indexOf("】");
							int pos2 = str.indexOf("【", pos1);

							String filmName = str.substring(pos1 + 2, pos2 - 4).replace("&nbsp;", "").trim();

							System.out.println("国产無碼filmName------------------->" + filmName); // 电影名称
							// String temp3 = "<a" + " href=\"";
							// String temp4 = "/\" target=\"_blank\">";
							String temp3 = "<a" + " href=\"";
							String temp4 = "target=\"_blank\"";
							// 下载url
							// String btUrl = str.substring(str.lastIndexOf(temp3) + temp3.length(),
							// str.lastIndexOf(temp4));
							String btUrl = str.substring(str.lastIndexOf(temp3) + temp3.length(),
									str.lastIndexOf(temp4) - 2);
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
							map.put("sn", "无");
							map.put("actor", "无");

							baseService = (BaseService) context.getBean("baseService");
							baseService.insertDB(map, "film");
						} catch (Exception e) {
							e.printStackTrace();
							page.putField("exception", e.getMessage()); // 当前文章界面
							System.out.println("国产無碼 Exception------------------------->" + e.getMessage());
						}
					}
				}

			}
		}

	}

}
