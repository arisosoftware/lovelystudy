package com.lovelystudy.web.tag;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lovelystudy.config.SiteConfig;
import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.Page;
import com.lovelystudy.module.notification.service.NotificationService;
import com.lovelystudy.module.user.pojo.User;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
@Component
public class NotificationsDirective extends BaseController implements TemplateDirectiveModel {

	@Autowired
	private NotificationService notificationService;
	@Autowired
	private SiteConfig siteConfig;

	@Override
	public void execute(Environment environment, Map map, TemplateModel[] templateModels,
			TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

		User user = getUser();

		int p = map.get("p") == null ? 1 : Integer.parseInt(map.get("p").toString());

		Page<Map> page = notificationService.findByTargetUserAndIsRead(p, siteConfig.getPageSize(), user, null);
		// 将未读消息置为已读
		notificationService.updateByIsRead(user);

		environment.setVariable("page", builder.build().wrap(page));
		templateDirectiveBody.render(environment.getOut());
	}
}