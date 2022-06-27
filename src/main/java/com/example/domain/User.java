package com.example.domain;

import java.sql.Timestamp;

public class User {
	private Integer id;
	private String name;
	private String ruby;
	private String mailAddress;
	private String zipCode;
	private String address;
	private String telephone;
	private String password;
	private Integer registUser;
	private Timestamp registDate;
	private Integer updateUser;
	private Timestamp updateDate;
	private Integer deleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
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

	public Integer getRegistUser() {
		return registUser;
	}

	public void setRegistUser(Integer registUser) {
		this.registUser = registUser;
	}

	public Timestamp getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Timestamp registDate) {
		this.registDate = registDate;
	}

	public Integer getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Integer updateUser) {
		this.updateUser = updateUser;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", ruby=" + ruby + ", mailAddress=" + mailAddress + ", zipCode="
				+ zipCode + ", address=" + address + ", telephone=" + telephone + ", password=" + password
				+ ", registUser=" + registUser + ", registDate=" + registDate + ", updateUser=" + updateUser
				+ ", updateDate=" + updateDate + ", deleted=" + deleted + "]";
	}

}
