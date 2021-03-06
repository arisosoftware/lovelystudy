package com.lovelystudy.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lovelystudy.core.base.BaseEntity;
import com.lovelystudy.module.user.pojo.User;

/**
 * 
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

	@Autowired
	private BaseEntity baseEntity;

	/**
	 * 如果session里没有用户信息，那么取cookie里的token，然后去数据库里查用户信息，再将用户信息存在session里
	 *
	 * @param request
	 * @param response
	 * @param o
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		User user = baseEntity.getUser();
		Assert.notNull(user, "请先登录，点击去<a href='/login'>登录</a>");
		return true;
	}

}
