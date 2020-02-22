package com.lovelystudy.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lovelystudy.config.properties.SiteConfig;
import com.lovelystudy.core.base.BaseEntity;
import com.lovelystudy.web.tag.CommentsDirective;
import com.lovelystudy.web.tag.CurrentUserDirective;
import com.lovelystudy.web.tag.NotificationsDirective;
import com.lovelystudy.web.tag.ReputationDirective;
import com.lovelystudy.web.tag.TopicsDirective;
import com.lovelystudy.web.tag.UserCollectDirective;
import com.lovelystudy.web.tag.UserCommentDirective;
import com.lovelystudy.web.tag.UserDirective;
import com.lovelystudy.web.tag.UserTopicDirective;

import freemarker.template.TemplateModelException;

@Component
public class FreemarkerConfig {

	@Autowired
	private BaseEntity baseEntity;

	@Autowired
	private CommentsDirective commentsDirective;
	@Autowired
	private freemarker.template.Configuration configuration;
	@Autowired
	private CurrentUserDirective currentUserDirective;
	private Logger log = LoggerFactory.getLogger(FreemarkerConfig.class);
	@Autowired
	private NotificationsDirective notificationsDirective;
	@Autowired
	private ReputationDirective reputationDirective;
	@Autowired
	private SecurityTag securityTag;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TopicsDirective topicsDirective;
	@Autowired
	private UserCollectDirective userCollectDirective;
	@Autowired
	private UserCommentDirective userCommentDirective;
	@Autowired
	private UserDirective userDirective;
	@Autowired
	private UserTopicDirective userTopicDirective;

	// The PostConstruct annotation is used on a method that needs to be executed
	// after dependency injection is done to perform any initialization. This method
	// MUST be invoked before the class is put into service.
	@PostConstruct
	public void setSharedVariable() throws TemplateModelException {
		// 注入全局配置至freemarker

		configuration.setSharedVariable("site", siteConfig);
		configuration.setSharedVariable("model", baseEntity);
		configuration.setSharedVariable("sec", securityTag);

		configuration.setSharedVariable("user_topics_tag", userTopicDirective);
		configuration.setSharedVariable("user_comments_tag", userCommentDirective);
		configuration.setSharedVariable("user_collects_tag", userCollectDirective);
		configuration.setSharedVariable("topics_tag", topicsDirective);
		configuration.setSharedVariable("user_tag", userDirective);
		configuration.setSharedVariable("current_user_tag", currentUserDirective);
		configuration.setSharedVariable("notifications_tag", notificationsDirective);
		configuration.setSharedVariable("comments_tag", commentsDirective);
		configuration.setSharedVariable("reputation_tag", reputationDirective);

		log.info("init freemarker sharedVariables {site} success...");
	}

}