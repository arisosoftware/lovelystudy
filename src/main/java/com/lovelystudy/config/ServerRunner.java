//package com.lovelystudy.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import com.corundumstudio.socketio.SocketIOServer;
//
//import com.lovelystudy.config.properties.SiteConfig;
//import com.lovelystudy.module.es.service.TagSearchService;
//import com.lovelystudy.module.es.service.TopicSearchService;
//
///**
// * 
// */
//@Component
//@Order(1)
//public class ServerRunner implements CommandLineRunner {
//
//	@Autowired
//	private SocketIOServer server;
//	@Autowired
//	private SiteConfig siteConfig;
//	@Autowired
//	private TagSearchService tagSearchService;
//	@Autowired
//	private TopicSearchService topicSearchService;
//
//	@Override
//	public void run(String... args) {
//		// 配置文件里开启了socket就启动服务，没有开启就不启动
//		if (siteConfig.isSocketNotification())
//			server.start();
//		if (siteConfig.isSearch()) {
//			topicSearchService.indexedAll();
//			tagSearchService.indexedAll();
//		}
//	}
//}
