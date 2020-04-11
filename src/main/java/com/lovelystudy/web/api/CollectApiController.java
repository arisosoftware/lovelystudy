package com.lovelystudy.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.Return;
import com.lovelystudy.core.exception.ApiAssert;
import com.lovelystudy.module.collect.pojo.Collect;
import com.lovelystudy.module.collect.service.CollectService;
import com.lovelystudy.module.topic.pojo.TopicWithBLOBs;
import com.lovelystudy.module.topic.service.TopicService;
import com.lovelystudy.module.user.pojo.User;

 
@RestController
@RequestMapping("/api/collect")
public class CollectApiController extends BaseController {

	@Autowired
	private CollectService collectService;
	@Autowired
	private TopicService topicService;

	@GetMapping("/add")
	public Return add(Integer topicId) {
		User user = getApiUser();
		TopicWithBLOBs topic = topicService.findById(topicId);

		ApiAssert.notNull(topic, "话题不存在");

		Collect collect = collectService.findByUserIdAndTopicId(getUser().getId(), topicId);
		ApiAssert.isNull(collect, "你已经收藏了这个话题");

		collectService.createCollect(topic, user.getId());

		return Return.success(collectService.countByTopicId(topicId));
	}

	@GetMapping("/delete")
	public Return delete(Integer topicId) {
		User user = getApiUser();
		Collect collect = collectService.findByUserIdAndTopicId(user.getId(), topicId);

		ApiAssert.notNull(collect, "你还没收藏这个话题");

		collectService.deleteById(collect.getId());
		return Return.success(collectService.countByTopicId(topicId));
	}
}
