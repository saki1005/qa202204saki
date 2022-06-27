package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.service.MailService;
import com.example.util.SessionUtil;
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
		mockMvc.perform(get("/register/email-submit").param("mailAddress", ""))
				.andExpect(view().name("email_submit.html"))
				.andReturn();
	}

	@Test
	@DisplayName("URLが発行できた場合：DB登録＆送信完了画面に遷移")
	@DatabaseSetup("classpath:email_submit_01-2.xlsx")
	@ExpectedDatabase(value = "classpath:email_submit_02.xlsx", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void emailSubmit_02() throws Exception {
		mockMvc.perform(get("/register/email-submit").param("mailAddress", "skweb39@gmail.com"))
				.andExpect(view().name("redirect:/register/email-finished")).andReturn();
	}
//
//	@Test
//	@DisplayName("URLが発行できた場合：DB登録＆送信完了画面に遷移")
//	@DatabaseSetup("classpath:email_submit_01-3.xlsx")
//	void emailSubmit_03() throws Exception {
//		mockMvc.perform(get("/register/email-submit").param("mailAddress", "skweb39@gmail.com"))
//				.andExpect(view().name("redirect:/register/email-finished")).andReturn();
//	}

	@Test
	@DisplayName("メールアドレス重複の場合送信完了画面表示")
	@DatabaseSetup("classpath:email_submit_05.xlsx")
	void emailSubmit_03() throws Exception {
		mockMvc.perform(get("/register/email-submit").param("mailAddress", "skweb39@gmail.com"))
				.andExpect(view().name("redirect:/register/email-finished")).andReturn();
	}

	@Test
	@DisplayName("すでに有効なURLが発行されていたらエラーメッセージを表示")
	@DatabaseSetup("classpath:email_submit_01.xlsx")
	void emailSubmit_04() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/register/email-submit").param("mailAddress", "skweb39@gmail.com"))
				.andExpect(view().name("email_submit.html")).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		String mes = (String) mav.getModel().get("message");
		assertEquals("すでに登録URLが発行されています。", mes, "メッセージが正しく取得できませんでした");
	}

	@Test
	@DisplayName("登録URLにアクセスしたら登録画面に遷移する")
	@DatabaseSetup("classpath:email_submit_03-2.xlsx")
	void insert_01() throws Exception {
		mockMvc.perform(get("/register/insert").param("key", "40c79cf6-bf1d-4f87-8cb9-95fb5a8fc619"))
				.andExpect(view().name("register_user.html")).andReturn();
	}

	@Test
	@DisplayName("会員登録正常")
	@DatabaseSetup("classpath:email_submit_03-2.xlsx")
	@ExpectedDatabase(value = "classpath:email_submit_04-2.xlsx", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void insert_02() throws Exception {
		MockHttpSession keySession = SessionUtil.createKeySession();
		mockMvc.perform(get("/register/finished").session(keySession).param("name", "山田太郎").param("ruby", "やまだたろう")
				.param("zipCode", "000-0000").param("address", "東京都新宿区").param("telephone", "000-0000-0000")
				.param("password", "test").param("confirmPassword", "test"))
				.andExpect(view().name("redirect:/register/toFinished")).andReturn();
	}

	@Test
	@DisplayName("パスワードが一致しなかった場合メッセージを表示")
	@DatabaseSetup("classpath:email_submit_03-2.xlsx")
	void insert_03() throws Exception {
		MockHttpSession keySession = SessionUtil.createKeySession();
		MvcResult mvcResult = mockMvc
				.perform(get("/register/finished").session(keySession).param("name", "山田太郎").param("ruby", "やまだたろう")
				.param("zipCode", "000-0000").param("address", "東京都新宿区").param("telephone", "000-0000-0000")
						.param("password", "test").param("confirmPassword", "miss"))
				.andExpect(view().name("register_user.html")).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		String mes = (String) mav.getModel().get("message");
		assertEquals("パスワードが一致しません", mes, "メッセージの取得に失敗しました");
	}

	@Test
	@DisplayName("登録フォーム：入力値エラーがあった場合登録画面を再表示")
	@DatabaseSetup("classpath:email_submit_03-2.xlsx")
	void insert_04() throws Exception {
		MockHttpSession keySession = SessionUtil.createKeySession();
		mockMvc.perform(get("/register/finished").session(keySession).param("ruby", "やまだたろう")
				.param("zipCode", "000-0000").param("address", "東京都新宿区").param("telephone", "000-0000-0000")
				.param("password", "test").param("confirmPassword", "test"))
				.andExpect(view().name("register_user.html")).andReturn();
	}

	@Test
	@DisplayName("URLが発行されていない状態で/finishedにアクセスした場合初期ページへ")
	void insert_05() throws Exception {
		mockMvc.perform(get("/register/finished")).andExpect(view().name("redirect:/register")).andReturn();
	}

	@Test
	@DisplayName("/registerにアクセスしたらメール送信画面を表示")
	void email_finished_01() throws Exception {
		mockMvc.perform(get("/register/email-finished")).andExpect(view().name("email_finished.html")).andReturn();
	}

	@Test
	@DisplayName("/registerにアクセスしたらメール送信画面を表示")
	void email_finished_0() throws Exception {
		mockMvc.perform(get("/register/toFinished")).andExpect(view().name("register_finished")).andReturn();
	}
}
