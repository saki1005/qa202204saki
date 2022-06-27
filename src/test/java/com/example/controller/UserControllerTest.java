package com.example.controller;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.service.MailService;
import com.example.util.XlsDataSetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
		TransactionDbUnitTestExecutionListener.class // @DatabaseSetupや@ExpectedDatabaseなどを使えるように指定
})

@SpringBootTest
class UserControllerTest {

	private static final UUID uuid = UUID.fromString("40c79cf6-bf1d-4f87-8cb9-95fb5a8fc619");

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate template;

	@Mock
	@Autowired
	private MailService mailService;

	@InjectMocks
	private UserController userController;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		MockedStatic<UUID> mocked = mockStatic(UUID.class);
		mocked.when(UUID::randomUUID).thenReturn(uuid);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(mockMvc);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("/registerにアクセスしたらメール送信画面を表示")
	void index() throws Exception {
		mockMvc.perform(get("/register")).andExpect(view().name("email_submit.html")).andReturn();
	}

	@Test
	@DisplayName("メール送信画面で入力値エラーがあった場合同じページを表示")
	void emailSubmit_01() throws Exception {
		mockMvc.perform(get("/register/email-submit").param("name", "")).andExpect(view().name("email_submit.html"))
				.andReturn();
	}

	@Test
	@DisplayName("すでに有効なURLが発行されていたらエラーメッセージを表示")
	@DatabaseSetup("classpath:email_submit_01.xlsx")
	void emailSubmit_02() throws Exception {
		mockMvc.perform(get("/register/email-submit").param("mailAddress", "skweb39@gmail.com"))
				.andExpect(view().name("email_submit.html")).andReturn();
	}

	@Test
	@DisplayName("URLが発行できた場合送信完了画面に遷移")
	@DatabaseSetup("classpath:email_submit_01.xlsx")
	void emailSubmit_03() throws Exception {
		mockMvc.perform(get("/register/email-submit").param("mailAddress", "skweb1005@gmail.com"))
				.andExpect(view().name("redirect:/register/email-finished")).andReturn();
	}

	@Test
	@DisplayName("URLが発行できた場合authenticatonsテーブルに登録")
	@DatabaseSetup("classpath:email_submit_01.xlsx")
	@ExpectedDatabase(value = "classpath:email_submit_02.xlsx", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void emailSubmit_04() throws Exception {

		mockMvc.perform(get("/register/email-submit").param("mailAddress", "skweb1005@gmail.com"))
				.andExpect(view().name("redirect:/register/email-finished")).andReturn();
	}

//	@Test
//	@DisplayName("会員登録正常")
//	@DatabaseSetup("classpath:email_submit_01.xlsx")
//	void emailSubmit_0() throws Exception {
//		mockMvc.perform(get("/email-submit").param("name", "山田太郎").param("ruby", "やまだたろう").param("zip_code", "000-0000")
//				.param("address", "東京都新宿区").param("telephone", "000-0000-0000").param("password", "test")
//				.param("confirmPassword", "test")).andExpect(view().name("email_submit.html")).andReturn();
//	}
}
