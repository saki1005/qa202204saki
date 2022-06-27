package com.example.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.mock.web.MockHttpSession;

public class SessionUtil {

	public static MockHttpSession createKeySession() {
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		sessionMap.put("key", "40c79cf6-bf1d-4f87-8cb9-95fb5a8fc619");
		return createMockHttpSession(sessionMap);
	}

	private static MockHttpSession createMockHttpSession(Map<String, Object> sessions) {
		MockHttpSession mockHttpSession = new MockHttpSession();
		for (Map.Entry<String, Object> session : sessions.entrySet()) {
			mockHttpSession.setAttribute(session.getKey(), session.getValue());
		}
		return mockHttpSession;
	}

}
