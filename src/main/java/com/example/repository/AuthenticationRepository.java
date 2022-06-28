package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Authentication;

@Repository
public class AuthenticationRepository {
	private static final RowMapper<Authentication> AUTHENTICATION_ROW_MAPPER = new BeanPropertyRowMapper<>(
			Authentication.class);

	@Autowired
	private NamedParameterJdbcTemplate template;

	public List<Authentication> findAuthentication(String email) {
		String sql = "SELECT * FROM authentications WHERE mail_address = :mailAddress AND deleted=0 AND register_date > (select now() + cast('-1 days' as INTERVAL))";

		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", email);
		List<Authentication> authenticationList = template.query(sql, param, AUTHENTICATION_ROW_MAPPER);
		System.out.println(authenticationList);
		return authenticationList;
	}

	public void insertAuthentication(Authentication authentication) {
		String sql = "INSERT INTO authentications(mail_address, unique_key, deleted)"
				+ " VALUES(:mailAddress, :uniqueKey, :deleted);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(authentication);
		template.update(sql, param);
	}

	public List<Authentication> findByKey(String key) {
		String sql = "SELECT * FROM authentications WHERE unique_key=:key AND register_date > (select now() + cast('-1 days' as INTERVAL)) AND deleted=0";
		SqlParameterSource param = new MapSqlParameterSource().addValue("key", key);
		List<Authentication> authenticationList = template.query(sql, param, AUTHENTICATION_ROW_MAPPER);
		return authenticationList;
	}

}
