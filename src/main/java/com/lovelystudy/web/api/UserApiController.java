package com.lovelystudy.web.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovelystudy.config.properties.SiteConfig;
import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.Page;
import com.lovelystudy.core.bean.Return;
import com.lovelystudy.core.exception.ApiAssert;
import com.lovelystudy.core.util.CookieHelper;
import com.lovelystudy.core.util.encrypt.Base64Helper;
import com.lovelystudy.core.util.identicon.Identicon;
import com.lovelystudy.module.collect.service.CollectService;
import com.lovelystudy.module.comment.service.CommentService;
import com.lovelystudy.module.log.service.LogService;
import com.lovelystudy.module.topic.service.TopicService;
import com.lovelystudy.module.user.pojo.User;
import com.lovelystudy.module.user.service.UserService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/user")
public class UserApiController extends BaseController {

	@Autowired
	private CollectService collectService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private Identicon identicon;
	@Autowired
	private LogService logService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TopicService topicService;
	@Autowired
	private UserService userService;

	/**
	 * 保存头像
	 *
	 * @param avatar 头像的Base64转码字符串
	 * @return
	 */
	@PostMapping("setting/changeAvatar")
	public Return changeAvatar(String avatar) {
		ApiAssert.notEmpty(avatar, "头像不能为空");
		String _avatar = avatar.substring(avatar.indexOf(",") + 1, avatar.length());
		User user = getUser();
		try {
			byte[] bytes = Base64Helper.decode(_avatar);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			BufferedImage bufferedImage = ImageIO.read(bais);
			String __avatar = null;
			if (siteConfig.getUploadType().equals("local")) {
				__avatar = identicon.saveFile(user.getUsername(), bufferedImage);
			}  
			
			user.setAvatar(__avatar);
			userService.update(user);
			bais.close();
		} catch (Exception e) {
			e.printStackTrace();
			return Return.error("头像格式不正确");
		}
		return Return.success();
	}

	/**
	 * 修改密码
	 *
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return
	 */
	@PostMapping("/setting/changePassword")
	public Return changePassword(String oldPassword, String newPassword) {
		User user = getApiUser();
		ApiAssert.notTrue(user.getBlock(), "你的帐户已经被禁用，不能进行此项操作");
		ApiAssert.isTrue(new BCryptPasswordEncoder().matches(oldPassword, user.getPassword()), "旧密码不正确");
		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		userService.update(user);
		return Return.success();
	}

	/**
	 * 用户的收藏
	 *
	 * @param username 用户名
	 * @param pageNo   页码
	 * @return
	 */
	@GetMapping("/{username}/collects")
	public Return collects(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
		User currentUser = userService.findByUsername(username);
		Page<Map> page = collectService.findByUserId(pageNo, siteConfig.getPageSize(), currentUser.getId());
		return Return.success(page);
	}

	/**
	 * 用户的评论
	 *
	 * @param username 用户名
	 * @param pageNo   页码
	 * @param pageSize 每页显示的条数，最多不能超过 siteConfig.getPageSize()
	 * @return
	 */
	@GetMapping("/{username}/comments")
	public Return comments(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo,
			Integer pageSize) {
		User currentUser = userService.findByUsername(username);
		Page<Map> page = commentService.findByUser(pageNo,
				pageSize > siteConfig.getPageSize() ? siteConfig.getPageSize() : pageSize, currentUser.getId());
		return Return.success(page);
	}

	/**
	 * 用户的个人信息
	 *
	 * @param username 用户名
	 * @return
	 */
	@GetMapping("/{username}")
	public Return profile(@PathVariable String username) {
		return Return.success(userService.findByUsername(username));
	}

	/**
	 * 刷新token
	 *
	 * @return
	 */
	@GetMapping("/setting/refreshToken")
	public Return refreshToken(HttpServletResponse response) {
		User user = getApiUser();
		user = userService.refreshToken(user);
		// 更新用户cookie
		CookieHelper.addCookie(response, siteConfig.getCookie().getDomain(), "/", siteConfig.getCookie().getUserName(),
				Base64Helper.encode(user.getToken().getBytes()), siteConfig.getCookie().getUserMaxAge() * 24 * 60 * 60,
				true, false);
		return Return.success();
	}

	/**
	 * 用户的日志列表
	 *
	 * @param pageNo 页码
	 * @return
	 */
	@GetMapping("/setting/log")
	public Return scoreLog(@RequestParam(defaultValue = "1") Integer pageNo) {
		User user = getApiUser();
		return Return.success(logService.findByUserId(pageNo, siteConfig.getPageSize(), user.getId()));
	}

	/**
	 * 用户的话题
	 *
	 * @param username 用户名
	 * @param pageNo   页码
	 * @param pageSize 每页显示的条数，最多不能超过 siteConfig.getPageSize()
	 * @return
	 */
	@GetMapping("/{username}/topics")
	public Return topics(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo,
			Integer pageSize) {
		User currentUser = userService.findByUsername(username);
		Page<Map> page = topicService.findByUser(pageNo,
				pageSize > siteConfig.getPageSize() ? siteConfig.getPageSize() : pageSize, currentUser.getId());
		return Return.success(page);
	}

	/**
	 * 更新用户的个人设置
	 *
	 * @param email        个人邮箱
	 * @param url
	 * @param bio
	 * @param commentEmail
	 * @param replyEmail
	 * @return
	 */
	@PostMapping("/setting/profile")
	public Return updateUserInfo(String email, String url, String bio,
			@RequestParam(defaultValue = "false") Boolean commentEmail,
			@RequestParam(defaultValue = "false") Boolean replyEmail) {
		User user = getApiUser();
		ApiAssert.notTrue(user.getBlock(), "你的帐户已经被禁用，不能进行此项操作");

		User updateUser = new User();
		updateUser.setId(user.getId());
//    updateUser.setEmail(email);
		if (bio != null && bio.trim().length() > 0)
			updateUser.setBio(Jsoup.clean(bio, Whitelist.none()));
		updateUser.setCommentEmail(commentEmail);
		updateUser.setReplyEmail(replyEmail);
		updateUser.setUrl(url);
		return Return.success(userService.update(updateUser));
	}
}
