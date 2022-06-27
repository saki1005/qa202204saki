package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Authentication;
import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repository;

	public List<Authentication> findAuthentication(String email) {
		List<Authentication> authenticationList = repository.findAuthentication(email);
		return authenticationList;
	}

	public List<Authentication> findByKey(String key) {
		List<Authentication> authenticationList = repository.findByKey(key);
		return authenticationList;
	}

	public List<User> findByMailAddress(String email) {
		List<User> userList = repository.findByMailAddress(email);
		return userList;
	}

	public void insertAuthentication(Authentication authentication) {
		repository.insertAuthentication(authentication);
	}

	public void insertUser(User user) {
		repository.insertUser(user);
	}

	public void updateAuthentication(String key) {
		repository.updateAuthentication(key);
	}
}
