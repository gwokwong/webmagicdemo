package com.weigw.sss.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import us.codecraft.webmagic.Page;

//灣搭专版
public class AquFid30 extends AbsAqu1024 {

	@Override
	public int getFid() {
		return 30;
	}

	@Override
	public void doDetailParse(Page page) {
		String nowUrl = page.getUrl().toString();
		String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
		HashMap<String, Object> map;
		String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
		try {
			// 图片地址
			List<String> ImgUrl = new ArrayList<>();
			Pattern p = Pattern.compile("<img src=\".*\" border=\"0\"");
			Matcher m = p.matcher(content);
			while (m.find()) {
				String url = m.group();
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

			// 下载url
			String btUrl = content.substring(content.indexOf(temp3, btUrlFindIndex) + temp3.length(), content.indexOf(temp4, btUrlFindIndex) - 2);
			map = new HashMap<>();

			String snValue = "无";
			String actorValue = "无";

			if (content.contains("品番")) {
				int snIndex = content.indexOf("品番");
				snValue = content.substring(snIndex + 3, content.indexOf("<br>", snIndex)).replace("&nbsp;", "");
			}

			if (content.contains("AV女優：")) {
				int actorIndex = content.indexOf("AV女優：");
				actorValue = content.substring(actorIndex + 5, content.indexOf("<br>", actorIndex)).replace("&nbsp;", "").replace("\n", "");
			}

			map.put("sn", snValue);
			map.put("actor", actorValue);
			map.put("film_name", field);
			map.put("bt_url", btUrl);
			map.put("img_url", StringUtils.collectionToDelimitedString(ImgUrl, ","));
			map.put("fid", getFid());
			map.put("source_url", nowUrl);
			System.out.println("map--->" + map.toString());
			bs.insertDB(map, "film");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
