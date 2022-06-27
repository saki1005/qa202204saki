package com.example.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired
	private JavaMailSender mailSender;

	public void sendHtmlMail(String email, String url) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("regist.user1005@gmail.com");
			helper.setTo(email);
			helper.setSubject("件名");
			helper.setText("本文",
					"Hogehogeシステム、新規ユーザー登録依頼を受け付けました。<br>" + "以下のURLから本登録処理を行ってください。<br>" + "Hogehogeシステム、ユーザー登録URL<br>"
							+ "<br>" + "<a href=\"" + url + "\">" + url + "</a>" + "<br>" + "※上記URLの有効期限は24時間以内です<br>");
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * keyをURLの末尾に埋め込むメソッド
	 * 
	 * @param key ランダムな文字列
	 * @return
	 */
	public String generateUrl(String key) {
		String url = "http://localhost:8080/user/register/insert" + "?key=" + key;
		return url;
	}
}
