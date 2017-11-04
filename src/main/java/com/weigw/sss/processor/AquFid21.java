package com.weigw.sss.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import us.codecraft.webmagic.Page;

//电驴迅雷
public class AquFid21 extends AbsAqu1024 {

	@Override
	public int getFid() {
		return 21;
	}

	@Override
	public void doDetailParse(Page page) {
		HashMap<String, Object> map = new HashMap<>();
		// fid
		map.put("fid", getFid());
		// 当前URL
		String nowUrl = page.getUrl().toString();
		map.put("source_url", nowUrl);

		// 标题（片名）
		String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
		String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();

		try {
			int pos1 = content.indexOf("】");
			int pos2 = content.indexOf("【", pos1);
			if (pos1 > 0 && pos2 > 0) {
				field = content.substring(pos1 + 2, pos2 - 4).replace("&nbsp;", "").trim();
			}
			map.put("film_name", field);

			// 图片
			List<String> ImgUrl = new ArrayList<>();
			Pattern p = Pattern.compile("<img src=\".*\" border=\"0\"");
			Matcher m = p.matcher(content);
			while (m.find()) {
				String url = m.group();
				String lastUrl = url.substring(url.indexOf("<img") + 10, url.indexOf("border") - 2);
				ImgUrl.add(lastUrl);
			}
			map.put("img_url", StringUtils.collectionToDelimitedString(ImgUrl, ","));

			// bt地址
			int btIndex = content.lastIndexOf("thunder");
			if (btIndex > 0) {
				String btUrl = content.substring(btIndex).replace("<br>", "");
				map.put("bt_url", btUrl);
			}
			// 番号
			if (content.contains("商品番号")) {
				int snIndex = content.indexOf("商品番号");
				if (snIndex > 0) {
					String snValue = content.substring(snIndex + 6, content.indexOf("<br>", snIndex)).replace("&nbsp;", "");
					map.put("sn", snValue);
				}
			}
			// 主角
			if (content.contains("影片主角")) {
				int actorIndex = content.indexOf("影片主角");
				String actorValue = content.substring(actorIndex + 6, content.indexOf("<br>", actorIndex)).replace("&nbsp;", "").replace("\n", "");
				map.put("actor", actorValue);
			}

			System.out.println("map--->" + map.toString());
			bs.insertDB(map, "film");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
