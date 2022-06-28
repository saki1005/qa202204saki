package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repository;

	public List<User> findByMailAddress(String email) {
		List<User> userList = repository.findByMailAddress(email);
		return userList;
	}

	public void insertUser(User user) {
		repository.insertUser(user);
	}

	public void updateAuthentication(String key) {
		repository.updateAuthentication(key);
	}
}
