package com.lovelystudy.core.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.lovelystudy.config.properties.SiteConfig;
import com.lovelystudy.core.exception.ApiAssert;
import com.lovelystudy.module.security.pojo.AdminUser;
import com.lovelystudy.module.user.pojo.User;

 
public class BaseController {

	@Autowired
	private BaseEntity baseEntity;
	@Autowired
	private SiteConfig siteConfig;

	protected AdminUser getAdminUser() {
		return baseEntity.getAdminUser();
	}

	/**
	 * 获取用户信息
	 *
	 * @return 没登录返回json
	 */
	protected User getApiUser() {
		User user = baseEntity.getUser();
		ApiAssert.notNull(user, "请先登录");
		return user;
	}

	/**
	 * 获取用户信息
	 *
	 * @return 没登录返回错误信息
	 */
	protected User getUser() {
		return baseEntity.getUser();
	}

	/**
	 * 带参重定向
	 *
	 * @param path
	 * @return
	 */
	protected String redirect(String path) {
		String baseUrl = siteConfig.getBaseUrl();
		baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		return "redirect:" + baseUrl + path;
	}

}
