package com.weigw.sss.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import us.codecraft.webmagic.Page;

//最新合集
public class AquFid3 extends AbsAqu1024 {

	@Override
	public int getFid() {
		return 3;
	}

	@Override
	public void doDetailParse(Page page) {
		String nowUrl = page.getUrl().toString();
		page.putField("currentUrl", page.getUrl()); // 当前文章界面
		String field = page.getHtml().xpath("//h1[@class='fl']//text()").toString();
		page.putField("field", field); // 当前正文
		if (field.contains("國產自拍")) {
			String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
			String[] itemArr = null;
			try {
				itemArr = content.replaceAll("\\[", "------").split("<br>\n" + "<br>\n" + "<br>\n" + "<br>------");
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
							filmName = str.substring(str.indexOf(temp21) + temp21.length() + 2, str.indexOf(temp22) - 5).replace("&nbsp;", "").trim();
							String temp3 = "<a" + " href=\"";
							String temp4 = "target=\"_blank\"";
							String btUrl = str.substring(str.lastIndexOf(temp3) + temp3.length(), str.lastIndexOf(temp4) - 2);
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
							map.put("sn", "无");
							map.put("actor", "无");
							System.out.println("map--->" + map.toString());
							bs.insertDB(map, "film");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (field.contains("国产高清")) {
			String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
			String[] itemArr = content
					.split("<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>");
			HashMap<String, Object> map;
			for (String str : itemArr) {
				if (str.length() > 200) {
					try {
						int pos1 = str.indexOf("】");
						int pos2 = str.indexOf("【", pos1);
						String filmName = str.substring(pos1 + 2, pos2 - 4).replace("&nbsp;", "").trim();
						String temp3 = "<a" + " href=\"";
						String temp4 = "target=\"_blank\"";
						// 下载url
						String btUrl = str.substring(str.lastIndexOf(temp3) + temp3.length(), str.lastIndexOf(temp4) - 2);
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
						map.put("sn", "无");
						map.put("actor", "无");
						System.out.println("map--->" + map.toString());
						bs.insertDB(map, "film");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (field.contains("国产無碼")) { // 和国产高清好像是一样的
			String content = page.getHtml().xpath("//div[@class='tpc_content']//html()").toString();
			String[] itemArr = content.split("<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>\n" + "<br>");
			HashMap<String, Object> map;
			for (String str : itemArr) {
				if (str.length() > 200) {
					try {
						int pos1 = str.indexOf("】");
						int pos2 = str.indexOf("【", pos1);
						String filmName = str.substring(pos1 + 2, pos2 - 4).replace("&nbsp;", "").trim();
						String temp3 = "<a" + " href=\"";
						String temp4 = "target=\"_blank\"";
						// 下载url
						String btUrl = str.substring(str.lastIndexOf(temp3) + temp3.length(), str.lastIndexOf(temp4) - 2);
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
