package com.weigw.sss.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import us.codecraft.webmagic.Page;

public class AquFid5 extends AbsAqu1024 {

	@Override
	public int getFid() {
		return 5;
	}

	@Override
	public void doDetailParse(Page page) {
		String nowUrl = page.getUrl().toString();
		String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
		HashMap<String, Object> map;
		String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
		try {
			String temp3 = "<a" + " href=\"";
			String temp4 = "target=\"_blank\"";
			// 下载url
			String btUrl = content.substring(content.lastIndexOf(temp3) + temp3.length(), content.lastIndexOf(temp4) - 2);
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
			map.put("fid", getFid());
			map.put("source_url", nowUrl);
			System.out.println("map--->" + map.toString());
			bs.insertDB(map, "film");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
