package com.lovelystudy.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lovelystudy.core.base.BaseEntity;
import com.lovelystudy.core.util.IpUtil;
import com.lovelystudy.module.user.pojo.User;

@Component
public class CommonInterceptor implements HandlerInterceptor {

	@Autowired
	private BaseEntity baseEntity;

	private Logger log = LoggerFactory.getLogger(CommonInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		long start = (long) request.getAttribute("_start");
		String actionName = request.getRequestURI();
		String clientIp = IpUtil.getIpAddr(request);
		StringBuilder logString = new StringBuilder();
		logString.append(clientIp).append("|").append(actionName).append("|");
		Map<String, String[]> params = request.getParameterMap();
		params.forEach((key, value) -> {
			logString.append(key);
			logString.append("=");
			for (String paramString : value) {
				logString.append(paramString);
			}
			logString.append("|");
		});
		long executionTime = System.currentTimeMillis() - start;
		logString.append("excitation=").append(executionTime).append("ms");
		log.info(logString.toString());
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		if (modelAndView != null) {
			User user = baseEntity.getUser();
			if (user != null)
				modelAndView.addObject("user", user);
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		long start = System.currentTimeMillis();
		request.setAttribute("_start", start);
		return true;
	}
}
