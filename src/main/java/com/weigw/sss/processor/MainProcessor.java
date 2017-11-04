package com.weigw.sss.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class MainProcessor implements PageProcessor {

	private String FID;
	String path = "1024";

	public MainProcessor(String FID) {
		this.FID = FID;
	}
	
	public void doRun() {
		String beginUrl = "http://w2.aqu1024.net/pw/thread.php?fid=" + FID + "&page=1";
		Spider.create(this).addUrl(beginUrl).addPipeline(new JsonFilePipeline(path)).thread(5)
		.run();
	}
	
	private Site site = Site.me().setSleepTime(1000).setUserAgent("safari");

	@Override
	public void process(Page page) {
		ProcessorParse parse = null;
		switch (FID) {
		case "3": // fid=3 最新合集
			parse = new Aqu1024fid3();
			break;
		case "22": // fid=22 日本骑兵
			parse = new Aqu1024fid22();
			break;
		case "30": // fid=30 灣搭专版
			parse = new Aqu1024fid30();
			break;
		case "39": // fid=39 多挂原创
			parse = new Aqu1024fid39();
			break;
		case "5": // fid=5 亞洲無碼
			parse = new Aqu1024fid5();
			break;
		case "21": // fid=21 电驴迅雷
//			parse = new Aqu1024fid21();
			parse = new AquFid21();
			break;
		case "37": // fid=21 电驴迅雷
			parse = new Aqu1024fid37();
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
		
		//39 多挂从64页后开始就是另外一种格式
		//3 最新合集  49
		//5 亚洲无码  61
		//22 日本骑兵  64 
		//21 电驴迅雷  59 
		//30 湾搭专区  140左右
		//37 魔王专区  140左右 
		
		String FID = "21";
//		String beginUrl = "http://w2.aqu1024.net/pw/thread.php?fid=" + FID + "&page=1";
//		String path = "1024";
//		Spider.create(new MainProcessor(FID)).addUrl(beginUrl).addPipeline(new JsonFilePipeline(path)).thread(5)
//				.run();
		
		new MainProcessor(FID).doRun();
	}

}
