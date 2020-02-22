package com.lovelystudy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;

import com.lovelystudy.config.properties.SiteConfig;

@org.springframework.context.annotation.Configuration
public class SocketConfig {

	@Autowired
	private SiteConfig siteConfig;

	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname(siteConfig.getSocket().getHostname());
		config.setPort(siteConfig.getSocket().getPort());

		// 时间单位（毫秒）
		// 协议升级超时时间（毫秒），默认10000。HTTP握手升级为ws协议超时时间
		config.setUpgradeTimeout(10 * 1000);
		config.setPingInterval(25000);
		config.setPingTimeout(60000);

		return new SocketIOServer(config);
	}

	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		return new SpringAnnotationScanner(socketServer);
	}

}