package com.example.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Authentication;
import com.example.domain.User;
import com.example.form.EmailSubmitForm;
import com.example.form.UserForm;
import com.example.service.MailService;
import com.example.service.UserService;

@Controller
@RequestMapping("/register")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	@Autowired
	private HttpSession session;

	@ModelAttribute
	public EmailSubmitForm setUpForm() {
		return new EmailSubmitForm();
	}

	@ModelAttribute
	public UserForm setUpForm2() {
		return new UserForm();
	}

	@RequestMapping("")
	public String index(Model model) {
		return "email_submit.html";
	}

	@RequestMapping("/email-submit")
	public String emailSubmit(@Validated EmailSubmitForm form, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			return index(model);
		}

		String email = form.getMailAddress();
		List<Authentication> authenticationList = null;
		List<User> userList = null;
		authenticationList = userService.findAuthentication(email);
		userList = userService.findByMailAddress(email);

		// 有効なURLがはっこうされていたらエラー
		if (authenticationList != null) {
			String mes = "すでに登録URLが発行されています。";
			model.addAttribute("message", mes);
			return index(model);

		}
		// メアド重複の場合送信完了画面へ
		if (userList != null) {
			String registEmail = userList.get(0).getMailAddress();
			if (form.getMailAddress().equals(registEmail)) {
				System.out.println("ユーザー重複");
				return "redirect:/register/email-finished";
			}
		}
		// 重複していない場合はkey発行、DB登録、メール送信、送信完了画面へ
		String uniqueKey = UUID.randomUUID().toString();
		// DB登録
		Authentication authentication = new Authentication();
		authentication.setMailAddress(email);
		authentication.setUniqueKey(uniqueKey);
		authentication.setDeleted(0);
		userService.insertAuthentication(authentication);
		System.out.println("DB登録");
		// メール送信（service）
		String url = mailService.generateUrl(uniqueKey);
		mailService.sendHtmlMail(email, url);

		System.out.println("送信完了");
		return "redirect:/register/email-finished";
	}

	@RequestMapping("/email-finished")
	public String EmailFinished() {
		return "email_finished.html";
	}

	@RequestMapping("/insert")
	public String index02(String key, Model model) {
		List<Authentication> authenticationList = userService.findByKey(key);
		if (authenticationList == null) {
			return "authentication_error.html";
		}
		session.setAttribute("key", key);
		System.out.println(key);
		return "register_user.html";
	}

	@RequestMapping("/finished")
	public String insert(@Validated UserForm form, BindingResult result, RedirectAttributes redirectAttributes,
			Model model) {
		String key = (String) session.getAttribute("key");
		System.out.println(key);
		if (key == null) {
			return "redirect:/register";
		}
		if (result.hasErrors()) {
			return "register_user.html";
		}
		if (!(form.getPassword().equals(form.getConfirmPassword()))) {
			model.addAttribute("message", "パスワードが一致しません");
			return "register_user.html";
		}
		List<Authentication> authenticationList = userService.findByKey(key);
		User user = new User();
		BeanUtils.copyProperties(form, user);
		user.setMailAddress(authenticationList.get(0).getMailAddress());
		userService.insertUser(user);
		userService.updateAuthentication(key);
		return "redirect:/register/toFinished";
	}

	@RequestMapping("/toFinished")
	public String toFinished() {
		return "register_finished";
	}
}
