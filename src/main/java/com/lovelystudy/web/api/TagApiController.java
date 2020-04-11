package com.lovelystudy.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelystudy.config.properties.SiteConfig;
import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.Return;
import com.lovelystudy.module.es.service.TagSearchService;
import com.lovelystudy.module.tag.service.TagService;

/**
 * 
 */
@RestController
@RequestMapping("/api/tag")
public class TagApiController extends BaseController {

	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TagSearchService tagSearchService;
	@Autowired
	private TagService tagService;

	/**
	 * 标签输入自动完成
	 *
	 * @param keyword 输入的内容
	 * @return
	 */
	@GetMapping("/autocomplete")
	public Return autocomplete(String keyword) {
		if (StringUtils.isEmpty(keyword))
			return Return.success();
		if (siteConfig.isSearch()) {
			return Return.success(tagSearchService.query(keyword, 7));
		} else {
			return Return.success(tagService.findByNameLike(keyword, 0, 7));
		}
	}

}
