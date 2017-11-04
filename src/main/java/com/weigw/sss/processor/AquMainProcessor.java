package com.weigw.sss.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class AquMainProcessor implements PageProcessor {

	private String FID;
	String path = "1024";

	public AquMainProcessor(String FID) {
		this.FID = FID;
	}

	public void doRun() {
		String beginUrl = "http://w2.aqu1024.net/pw/thread.php?fid=" + FID + "&page=1";
		Spider.create(this).addUrl(beginUrl).addPipeline(new JsonFilePipeline(path)).thread(5).run();
	}

	private Site site = Site.me().setSleepTime(1000).setUserAgent("safari");

	@Override
	public void process(Page page) {
		ProcessorParse parse = null;
		switch (FID) {
		case "3": // fid=3 最新合集
			parse = new AquFid3();
			break;
		case "22": // fid=22 日本骑兵
			parse = new AquFid22();
			break;
		case "30": // fid=30 灣搭专版
			parse = new AquFid30();
			break;
		case "39": // fid=39 多挂原创
			parse = new AquFid39();
			break;
		case "5": // fid=5 亞洲無碼
			parse = new AquFid5();
			break;
		case "21": // fid=21 电驴迅雷
			parse = new AquFid21();
			break;
		default:
			break;
		}
		parse.parse(page);
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		String FID = "21";
		new AquMainProcessor(FID).doRun();
	}

}
