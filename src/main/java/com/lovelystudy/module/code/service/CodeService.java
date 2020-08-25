package com.lovelystudy.module.code.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lovelystudy.core.util.DateUtil;
import com.lovelystudy.core.util.StrUtil;
import com.lovelystudy.module.code.mapper.CodeMapper;
import com.lovelystudy.module.code.pojo.Code;
import com.lovelystudy.module.code.pojo.CodeEnum;

@Service
@Transactional
public class CodeService {

	@Autowired
	private CodeMapper codeMapper;

	public Code findByEmailAndCodeAndType(String email, String code, CodeEnum type) {
		List<Code> codes = codeMapper.findByEmailAndCodeAndType(email, code, type.name());
		if (codes.size() > 0)
			return codes.get(0);
		return null;
	}

	public String genEmailCode(String email) {
		String genCode = StrUtil.randomString(6);
		Code code = findByEmailAndCodeAndType(email, genCode, CodeEnum.EMAIL);
		if (code != null) {
			return genEmailCode(email);
		} else {
			code = new Code();
			code.setCode(genCode);
			code.setExpireTime(DateUtil.getMinuteAfter(new Date(), 10));
			code.setType(CodeEnum.EMAIL.name());
			code.setEmail(email);
			code.setIsUsed(false);
			save(code);
			return genCode;
		}
	}

	public void save(Code code) {
		codeMapper.insertSelective(code);
	}

	public void update(Code code) {
		codeMapper.updateByPrimaryKeySelective(code);
	}

	public int validateCode(String email, String code, CodeEnum type) {
		Code code1 = findByEmailAndCodeAndType(email, code, type);
		if (code1 == null)
			return 1;// 验证码不正确
		if (DateUtil.isExpire(code1.getExpireTime()))
			return 2; // 过期了
		if (code1.getIsUsed())
			return 3; // 验证码已经被使用了
		code1.setIsUsed(true);
		update(code1);
		return 0; // 正常
	}
}
