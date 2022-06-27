package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailSubmitForm {
	@NotBlank(message = "メールアドレスを入力してください。")
	@Email(message = "正しい形式で入力してください。")
	private String mailAddress;

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
}
