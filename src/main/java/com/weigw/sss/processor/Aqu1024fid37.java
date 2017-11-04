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

//魔王专区
public class Aqu1024fid37 extends ProcessorParse {

	private static final String FID = "37";
	private BaseService baseService;
	private ApplicationContext context;

	public Aqu1024fid37() {
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
			
			
			
			
			//TODO  未完成
			
			
			
			String nowUrl = page.getUrl().toString();
			// page.putField("currentUrl", page.getUrl()); // 当前文章界面
			String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
			// page.putField("field", field); // 这个就是名字
			System.out.println("灣搭专版file_name--->" + field);
			HashMap<String, Object> map;

			String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
			// System.out.println("content---->"+content);
			try {

				// 图片地址
				List<String> ImgUrl = new ArrayList<>();
				Pattern p = Pattern.compile("<img src=\".*\" border=\"0\"");
				Matcher m = p.matcher(content);
				while (m.find()) {
					String url = m.group();
					System.out.println("find url----->" + url);
					String lastUrl = url.substring(url.indexOf("<img") + 10, url.indexOf("border") - 2);
					if (!url.contains("sinaimg") && !url.contains("51688.cc") && !url.contains("https://www1.wi.to")) {
						ImgUrl.add(lastUrl);
					}
				}

				String temp3 = "<a" + " href=\"";
				String temp4 = "target=\"_blank\"";

				int btUrlFindIndex = 0;
				if (ImgUrl.size() > 1) {
					btUrlFindIndex = content.indexOf(ImgUrl.get(ImgUrl.size() - 1));
				}

				// 取图片最后一张的索引为起止点
				// btUrlFindIndex =
				// content.indexOf(ImgUrl.get(ImgUrl.size()-1))>0?content.indexOf(ImgUrl.get(ImgUrl.size()-1)):0;
				// 下载url
				String btUrl = content.substring(content.indexOf(temp3, btUrlFindIndex) + temp3.length(),
						content.indexOf(temp4, btUrlFindIndex) - 2);
				map = new HashMap<>();

				String snValue = "无";
				String actorValue = "无";

				if (content.contains("品番")) {
					int snIndex = content.indexOf("品番");
					snValue = content.substring(snIndex + 3, content.indexOf("<br>", snIndex)).replace("&nbsp;", "");
					// map.put("sn", content.substring(snIndex+1,content.indexOf("\n" + "<br>",
					// snIndex)).replace("&nbsp;", ""));
				}

				if (content.contains("AV女優：")) {
					int actorIndex = content.indexOf("AV女優：");
					actorValue = content.substring(actorIndex + 5, content.indexOf("<br>", actorIndex))
							.replace("&nbsp;", "").replace("\n", "");
					// map.put("actor", content.substring(actorIndex+1,content.indexOf("\n" +
					// "<br>", actorIndex)).replace("&nbsp;", ""));
				}

				map.put("sn", snValue);
				map.put("actor", actorValue);
				map.put("film_name", field);
				map.put("bt_url", btUrl);
				map.put("img_url", StringUtils.collectionToDelimitedString(ImgUrl, ","));
				map.put("fid", FID);
				map.put("source_url", nowUrl);
				System.out.println(map.toString());

				baseService = (BaseService) context.getBean("baseService");
				baseService.insertDB(map, "film");
			} catch (Exception e) {
				e.printStackTrace();
				page.putField("exception", e.getMessage());
				System.out.println(
						"灣搭专版 now url" + page.getUrl() + "\nException------------------------->" + e.getMessage());
			}
		}

	}

}
