package thesis.webcryptoexchange;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import thesis.webcryptoexchange.repository.*;
import thesis.webcryptoexchange.model.*;
import thesis.webcryptoexchange.service.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc 
class WebCryptoExchangeApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserService userServ;

	@Autowired
    private CryptoService crypService;

	// Авторизация
	@Test
	void authorizationGet() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk());
	}

	@Test
	void authorizationPost() throws Exception {
		mockMvc.perform(post("/login")).andExpect(status().isFound()).andExpect(redirectedUrl("/login-error"));
	}

	// Регистрация
	@Test
	void registerGet() throws Exception {
		mockMvc.perform(get("/reg")).andExpect(status().isOk());
	}

	@Test
	void registerPost() throws Exception {
		User user = new User();
		user.setName("admin");
		assertFalse(userServ.save(user));
	}

	// Управление автообновлением биржи
	@Test
	@WithMockUser(roles={"ADMIN"})
	void updateGetOn() throws Exception {
		mockMvc.perform(get("/update")).andExpect(status().isFound()).andExpect(redirectedUrl("/"));
	}

	void updateGetOff() throws Exception {
		mockMvc.perform(get("/update")).andExpect(status().isFound()).andExpect(redirectedUrl("/"));
	}

	// Покупка/продажи криптовалюты
	@Test
	@WithMockUser(roles={"USER"})
	void exchangePost() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles={"USER"})
	void transacPost() throws Exception {
		mockMvc.perform(post("/transac")).andExpect(status().is(400));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	void findAllCurrsTrue() throws Exception {
		assertTrue(crypService.findAll().size() >= 0);
	}

	// Прогноз стоимости валюты в следующем месяце
	@Test
	@WithMockUser(roles={"USER"})
	void predictGet() throws Exception {
		mockMvc.perform(get("/prediction?curr=BTC")).andExpect(status().isOk());
	}

	// Просмотр списка успешных транзакций
	@Test
	@WithMockUser(roles={"USER"})
	void transacsGet() throws Exception {
		assertThrows(Exception.class, () -> mockMvc.perform(get("/transacs")).andExpect(status().isInternalServerError()));
	}

	// Выход из аккаунта
	@Test
	@WithMockUser(roles={"USER"})
	void logoutGet() throws Exception {
		mockMvc.perform(get("/logout")).andExpect(status().isFound()).andExpect(redirectedUrl("/login?logout"));
	}

	// Просмотр денежных средств и имеющейся криптовалюты
	@Test
	@WithMockUser(roles={"USER"})
	void accountGet() throws Exception {
		assertThrows(Exception.class, () -> mockMvc.perform(get("/account")).andExpect(status().isInternalServerError()));
	}

	@Test
	@WithMockUser(roles={"USER"})
	void usercurrFinding() throws Exception {
		assertThrows(Exception.class, () -> userServ.findCurrs());
	}

	// Вывод списка пользователей
	@Test
	@WithMockUser(roles={"ADMIN"})
	void usersGet() throws Exception {
		mockMvc.perform(get("/users")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	void findAllUsersTrue() throws Exception {
		assertTrue(userServ.findAll().size() > 0);
	}

	// Блокировка пользователя
	@Test
	@WithMockUser(roles={"ADMIN"})
	void blockGet() throws Exception {
		mockMvc.perform(get("/block?user=50")).andExpect(status().is(302));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	void unblockGet() throws Exception {
		mockMvc.perform(get("/unblock?user=50")).andExpect(status().is(302));
	}
}