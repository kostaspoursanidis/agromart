package com.controllerTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.Model.User;
import com.Repos.UserRepo;
import com.Services.UserService;


@SpringBootTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureMockMvc(addFilters=false)
public class RegisterAndLoginControllerTest {

	//@Autowired
	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	private User getMockUserByRole(String given_role) {
		User user = new User();
		user.setId(1l);
		user.setName("name");
		user.setEmail("email@email.com");
		user.setPassword("password");
		user.setRole(given_role);
		user.setAddress("address");
		user.setPhoneNum("123456789");
		user.setPhoto(null);
							
		return user;
	}
	
	@BeforeAll
	void setUp() {
		mvc = MockMvcBuilders
	            .webAppContextSetup(context)
	            .apply(springSecurity())
	            .build();
	}
	
	@AfterEach
	void tearDown() {
		userRepo.deleteAll();
	}
	
	@Test
	void canGetRegisterPage() throws Exception {
		
		mvc.perform(get("/register")).
				andExpect(status().isOk()).
				andExpect(model().attributeExists("user")).
				andExpect(view().name("register"));
		
	}
	
	@Test
	void canSaveUserAndReturnPageSuccess() throws Exception {
		User mock_user = getMockUserByRole("producer");
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("name", mock_user.getName());
		multiValueMap.add("email", mock_user.getEmail());
		multiValueMap.add("password", mock_user.getPassword());
		multiValueMap.add("phoneNum", mock_user.getPhoneNum());
		multiValueMap.add("address", mock_user.getAddress());
		multiValueMap.add("role", mock_user.getRole());
		mvc.perform(post("/register")
				.params(multiValueMap))
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/register?success"));
		
	}
	
	@Test
	void willReturnPageWithParamFailureIfUserWithEmailGivenExists() throws Exception {
		User mock_user = getMockUserByRole("producer");
		
		userRepo.save(mock_user);
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("name", mock_user.getName());
		multiValueMap.add("email", mock_user.getEmail());
		multiValueMap.add("password", mock_user.getPassword());
		multiValueMap.add("phoneNum", mock_user.getPhoneNum());
		multiValueMap.add("address", mock_user.getAddress());
		multiValueMap.add("role", mock_user.getRole());
		mvc.perform(post("/register")
				.params(multiValueMap))
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/register?failure"));
		
	}
	
	
	@Test
	void canGetLoginPage() throws Exception {
		mvc.perform(get("/login"))
		.andExpect(status().isOk())
		.andExpect(view().name("login"));
	}
	
	
	
	@Test
	void canLoginWithSavedUser() throws Exception {
		User mock_user = getMockUserByRole("producer");
		userService.save(mock_user);
		
		mvc.perform(formLogin().user("email@email.com").password("password"))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/profile"))
		.andExpect(authenticated().withUsername("email@email.com"));
	}
	
	@Test
	void wrongCredentialsWillReturnErrorPage() throws Exception {
		
		
		mvc.perform(formLogin().user( "notExists@example").password("wrongpass"))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/login?error"));
	}
	
	@Test
	void canLogout() throws Exception {
		User mock_user = getMockUserByRole("producer");
		userService.save(mock_user);
		
		mvc.perform(formLogin().user("email@email.com").password("password"))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/profile"))
		.andExpect(authenticated().withUsername("email@email.com"));
		
		mvc.perform(logout())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/login?logout"));
	}
	
	
	
	
}
