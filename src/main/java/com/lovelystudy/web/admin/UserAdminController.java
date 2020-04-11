package com.lovelystudy.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lovelystudy.config.properties.SiteConfig;
import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.Return;
import com.lovelystudy.core.exception.ApiAssert;
import com.lovelystudy.module.user.pojo.User;
import com.lovelystudy.module.user.service.UserService;

/**
 * 
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController extends BaseController {

	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private UserService userService;

	@GetMapping("/block")
	@ResponseBody
	public Return block(Integer id) {
		userService.blockUser(id);
		return Return.success();
	}

	@GetMapping("/delete")
	@ResponseBody
	public Return delete(Integer id) {
		userService.deleteById(id);
		return Return.success();
	}

	@GetMapping("/edit")
	public String edit(Integer id, Model model) {
		model.addAttribute("user", userService.findById(id));
		return "admin/user/edit";
	}

	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
		model.addAttribute("page", userService.pageUser(pageNo, siteConfig.getPageSize()));
		return "admin/user/list";
	}

	@PostMapping("/edit")
	@ResponseBody
	public Return update(Integer id, String username, String password, Integer reputation) {
		ApiAssert.notEmpty(username, "用户名不能为空");
		ApiAssert.notNull(reputation, "声望不能为空");
		User user = userService.findById(id);
		user.setUsername(username);
		// 如果密码字段存在，则修改
		if (!StringUtils.isEmpty(password)) {
			user.setPassword(new BCryptPasswordEncoder().encode(password));
		}
		user.setReputation(reputation);
		userService.update(user);
		return Return.success();
	}

}
