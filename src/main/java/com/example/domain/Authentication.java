package com.example.domain;

import java.sql.Timestamp;

public class Authentication {
	private Integer id;
	private String mailAddress;
	private String uniqueKey;
	private Timestamp registDate;
	private Integer deleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public Timestamp getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Timestamp registDate) {
		this.registDate = registDate;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "RegistUrl [id=" + id + ", mailAddress=" + mailAddress + ", key=" + uniqueKey + ", registDate=" + registDate
				+ ", deleted=" + deleted + "]";
	}
}
