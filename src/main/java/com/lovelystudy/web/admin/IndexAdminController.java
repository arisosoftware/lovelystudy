package com.lovelystudy.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lovelystudy.config.properties.SiteConfig;
import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.Result;
import com.lovelystudy.core.util.CookieHelper;
import com.lovelystudy.module.es.service.TagSearchService;
import com.lovelystudy.module.es.service.TopicSearchService;
import com.lovelystudy.module.security.service.AdminUserService;
import com.lovelystudy.module.user.service.UserService;
 
@Controller
@RequestMapping("/admin")
public class IndexAdminController extends BaseController {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TagSearchService tagSearchService;
	@Autowired
	private TopicSearchService topicSearchService;
	@Autowired
	private UserService userService;

	@GetMapping("/clear")
	@ResponseBody
	public Result clear(Integer type) {
		if (type == 1) {
			userService.deleteAllRedisUser();
		} else if (type == 2) {
			adminUserService.deleteAllRedisAdminUser();
		}
		return Result.success();
	}

	@GetMapping("/index")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/indexedTag")
	@ResponseBody
	public Result indexedTag() {
		tagSearchService.indexedAll();
		return Result.success();
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		CookieHelper.clearCookieByName(request, response, siteConfig.getCookie().getAdminUserName(),
				siteConfig.getCookie().getDomain(), "/admin/");
		return redirect("/admin/login");
	}

	@GetMapping("/indexedTopic")
	@ResponseBody
	public Result topicIndexed() {
		topicSearchService.indexedAll();
		return Result.success();
	}
}
