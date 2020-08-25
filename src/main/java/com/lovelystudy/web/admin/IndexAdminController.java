package com.lovelystudy.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lovelystudy.config.SiteConfig;
import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.ResponseBean;
import com.lovelystudy.core.util.CookieHelper;

import com.lovelystudy.module.security.service.AdminUserService;
import com.lovelystudy.module.tag.service.TagService;
import com.lovelystudy.module.topic.service.TopicService;
import com.lovelystudy.module.user.service.UserService;

@Controller
@RequestMapping("/admin")
public class IndexAdminController extends BaseController {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TagService tagSearchService;
	@Autowired
	private TopicService topicSearchService;
	@Autowired
	private UserService userService;

	@GetMapping("/clear")
	@ResponseBody
	public ResponseBean clear(Integer type) {
		if (type == 1) {
			userService.deleteAllRedisUser();
		} else if (type == 2) {
			adminUserService.deleteAllRedisAdminUser();
		}
		return ResponseBean.success();
	}

	@GetMapping("/index")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/indexedTag")
	@ResponseBody
	public ResponseBean indexedTag() {
		tagSearchService.indexedAll();
		return ResponseBean.success();
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		CookieHelper.clearCookieByName(request, response, siteConfig.getCookie().getAdminUserName(),
				siteConfig.getCookie().getDomain(), "/admin/");
		return redirect("/admin/login");
	}

	@GetMapping("/indexedTopic")
	@ResponseBody
	public ResponseBean topicIndexed() {
		// TODO:add index all feature topicSearchService).indexedAll();
		return ResponseBean.success();
	}
}
