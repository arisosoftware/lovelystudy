package com.lovelystudy.core.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class EmailUtil {

	@Autowired
	private Environment env;

	@Autowired
	private JavaMailSender javaMailSender;
	private Logger log = LoggerFactory.getLogger(EmailUtil.class);

	public void sendEmail(String email, String subject, String content) {
		new Thread(() -> {
			try {
				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
				helper.setFrom(env.getProperty("spring.mail.username"));
				helper.setTo(email);

				helper.setSubject(subject);
				helper.setText(content, true);
				javaMailSender.send(mimeMessage);
			} catch (MessagingException e) {
				log.error(e.getLocalizedMessage());
			}
		}).start();
	}
}
