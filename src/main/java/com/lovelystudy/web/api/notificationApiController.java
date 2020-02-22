package com.lovelystudy.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.Result;
import com.lovelystudy.core.exception.ApiException;
import com.lovelystudy.module.notification.service.NotificationService;
import com.lovelystudy.module.user.pojo.User;

/**
 * 
 */
@RestController
@RequestMapping("/api/notification")
public class notificationApiController extends BaseController {

	@Autowired
	private NotificationService notificationService;

	/**
	 * 查询当前用户未读的消息数量
	 *
	 * @return
	 */
	@GetMapping("/notRead")
	public Result notRead() throws ApiException {
		User user = getApiUser();
		return Result.success(notificationService.countByTargetUserAndIsRead(user, false));
	}
}
