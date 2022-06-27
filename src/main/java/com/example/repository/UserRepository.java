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

import com.example.domain.Authentication;
import com.example.domain.User;

/**
 * usersテーブルを操作するリポジトリです。
 * 
 * @author ishigamisaki
 */

@Repository
public class UserRepository {
	private static final RowMapper<Authentication> AUTHENTICATION_ROW_MAPPER = new BeanPropertyRowMapper<>(
			Authentication.class);
	private static final RowMapper<User> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

	@Autowired
	private NamedParameterJdbcTemplate template;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Authentication> findAuthentication(String email) {
		String sql = "SELECT * FROM authentications WHERE mail_address = :mailAddress AND deleted=0 AND register_date > (select now() + cast('-1 days' as INTERVAL))";

		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", email);
		List<Authentication> registUrlList = template.query(sql, param, AUTHENTICATION_ROW_MAPPER);
		if (registUrlList.size() == 0) {
			return null;
		}
		System.out.println(registUrlList);
		return registUrlList;
	}

	public void insertAuthentication(Authentication authentication) {
		String sql = "INSERT INTO authentications(mail_address, unique_key, deleted)"
				+ " VALUES(:mailAddress, :uniqueKey, :deleted);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(authentication);
		template.update(sql, param);
	}

	public List<Authentication> findByKey(String key) {
		String sql = "SELECT * FROM authentications WHERE unique_key=:key AND deleted=0";
		SqlParameterSource param = new MapSqlParameterSource().addValue("key", key);
		List<Authentication> authenticationList = template.query(sql, param, AUTHENTICATION_ROW_MAPPER);
		if (authenticationList.size() == 0) {
			return null;
		}
		return authenticationList;
	}

	public void updateAuthentication(String key) {
		String sql = "UPDATE authentications SET deleted=1 WHERE unique_key=:key";
		SqlParameterSource param = new MapSqlParameterSource().addValue("key", key);
		template.update(sql, param);
	}

	public List<User> findByMailAddress(String email) {
		String sql = "SELECT * FROM users WHERE mail_address = :mailAddress AND deleted=0";

		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", email);
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		System.out.println(userList);
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
