package com.example.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserForm {
	@NotBlank(message = "名前を入力してください。")
	private String name;
	@NotBlank(message = "ふりがなを入力してください。")
	@Pattern(regexp = "^[あ-んー　]*$", message = "全角ひらがなで入力してください")
	private String ruby;
	@NotBlank(message = "郵便番号を入力してください。")
	@Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "XXX-XXXXの形式で入力してください")
	private String zipCode;
	@NotBlank(message = "住所を入力してください。")
	private String address;
	@NotBlank(message = "電話番号を入力してください。")
	@Pattern(regexp = "^[0-9]{3}-[0-9]{4}-[0-9]{4}$", message = "XXX-XXXX-XXXXの形式で入力してください")
	private String telephone;
	@NotBlank(message = "パスワードを入力してください。")
	private String password;
	@NotBlank(message = "確認パスワードを入力してください。")
	private String confirmPassword;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRuby() {
		return ruby;
	}

	public void setRuby(String ruby) {
		this.ruby = ruby;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "RegistUserForm [name=" + name + ", ruby=" + ruby + ", zipCode=" + zipCode + ", address=" + address
				+ ", telephone=" + telephone + ", password=" + password + ", confirmPassword=" + confirmPassword + "]";
	}

}
