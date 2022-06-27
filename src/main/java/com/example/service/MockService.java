package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class MockService {
	public String generateKey() {
		// 指定の文字によるリストを作成
		List<Character> letters = new ArrayList<>();
		char lowercaseAlphabet = 'a';
		for (int i = 1; i <= 26; i++) {// 理解できなければ参照：https://inarizuuuushi.hatenablog.com/entry/2017/05/31/173343
			letters.add(lowercaseAlphabet++);
		}
		char uppercaseAlphabet = 'A';
		for (int i = 1; i <= 26; i++) {
			letters.add(uppercaseAlphabet++);
		}
		char number = '0';
		for (int i = 1; i <= 10; i++) {
			letters.add(number++);
		}

		// ランダムなkeyを生成
		String key = "";
		int digit = 20;// 桁数の指定
		Random random = new Random();
		for (int i = 1; i <= digit; i++) {
			int randomNumber = random.nextInt(letters.size() - 1);
			key += letters.get(randomNumber);
		}
		return key;
	}
}
