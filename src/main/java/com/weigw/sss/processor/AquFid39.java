package com.weigw.sss.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import us.codecraft.webmagic.Page;

public class AquFid39 extends AbsAqu1024 {

	@Override
	public int getFid() {
		return 39;
	}

	@Override
	public void doDetailParse(Page page) {
		String nowUrl = page.getUrl().toString();
		page.putField("currentUrl", page.getUrl()); // 当前文章界面
		String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
		if (field.contains("經典推薦")) {
			String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
			String[] itemArr = content.split("<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n"
					+ "<br>﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋﹋\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>");
			HashMap<String, Object> map;
			for (String str : itemArr) {
				if (str.length() > 200) {
					try {
						String filmName = "";
						int pos1 = str.indexOf("】");
						int pos2 = str.indexOf("【", pos1);
						filmName = str.substring(pos1 + 2, pos2 - 4).replace("&nbsp;", "").trim();
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
						map.put("fid", getFid());
						map.put("source_url", nowUrl);
						System.out.println("map--->" + map.toString());
						bs.insertDB(map, "film");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
