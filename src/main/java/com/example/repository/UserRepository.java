package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

/**
 * usersテーブルを操作するリポジトリです。
 * 
 * @author ishigamisaki
 */

@Repository
public class UserRepository {

	private static final RowMapper<User> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

	@Autowired
	private NamedParameterJdbcTemplate template;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void updateAuthentication(String key) {
		String sql = "UPDATE authentications SET deleted=1 WHERE unique_key=:key";
		SqlParameterSource param = new MapSqlParameterSource().addValue("key", key);
		template.update(sql, param);
	}

	public List<User> findByMailAddress(String email) {
		String sql = "SELECT * FROM users WHERE mail_address = :mailAddress AND deleted=0";

		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", email);
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		return userList;
	}

	public void insertUser(User user) {
		String sql = "INSERT INTO users(name,ruby,mail_address,zip_code,address,telephone,password)"
				+ " VALUES(:name,:ruby,:mailAddress,:zipCode,:address,:telephone,:password);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		template.update(sql, param);
	}

}
