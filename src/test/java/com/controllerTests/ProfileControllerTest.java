package com.controllerTests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.web.context.WebApplicationContext;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.Model.User;
import com.Repos.FollowerRepo;
import com.Repos.FruitRepo;
import com.Repos.RatingRepo;
import com.Repos.UserRepo;
import com.Services.FollowerService;
import com.Services.FruitService;
import com.Services.RatingService;
import com.Services.UserDetailsImpl;
import com.Services.UserService;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
public class ProfileControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	//@Autowired
	//private WebApplicationContext context;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private RatingService ratingService;
	
	@Autowired
	private RatingRepo ratingRepo;

    @Autowired
    private FollowerService followerService;
    
    @Autowired
    private FollowerRepo followerRepo;

    @Autowired
    private FruitService fruitService;
    
    @Autowired
    private FruitRepo fruitRepo;
    
    
    private User loggedInUser;
    private User otherUser;
    

	
    private User getMockUserByRole(String given_role) {
		User user = new User();
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
	void setUp() throws Exception {
		/*mvc = MockMvcBuilders
	            .webAppContextSetup(context)
	            //.apply(springSecurity())
	            .build();*/
		
		loggedInUser = getMockUserByRole("producer");
		userService.save(loggedInUser);
		
		otherUser = getMockUserByRole("producer");
		otherUser.setEmail("other@email.com");
		userService.save(otherUser);
	}
    
    @AfterAll
    void tearDown() {
    	userRepo.deleteAll();
    	fruitRepo.deleteAll();
    	followerRepo.deleteAll();
    	ratingRepo.deleteAll();
    	
    }
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canGetCurrentlyLoggedInUserProfile() throws Exception {
		
		mvc.perform(get("/profile")).
			andExpect(status().isOk()).
			andExpect(model().attributeExists("user")).
			andExpect(model().attributeExists("curruser")).
			andExpect(model().attributeExists("fruitList")).
			andExpect(model().attributeExists("addressQ")).
			andExpect(MockMvcResultMatchers.model().attribute("user", hasProperty("email", equalTo("email@email.com")))).
			andExpect(view().name("profilePage"));
	}
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canGetOtherUsersProfiles() throws Exception {
		
		mvc.perform(get("/profile/"+otherUser.getId())).
			andExpect(status().isOk()).
			andExpect(model().attributeExists("user")).
			andExpect(model().attributeExists("isFollowed")).
			andExpect(model().attributeExists("curruser")).
			andExpect(model().attributeExists("fruitList")).
			andExpect(model().attributeExists("addressQ")).
			andExpect(MockMvcResultMatchers.model().attribute("user", hasProperty("email", equalTo("other@email.com")))).
			andExpect(view().name("profilePage"));
	}
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canGetProfileEditPage() throws Exception {
		
		mvc.perform(get("/profile/edit/"+otherUser.getId())).
			andExpect(status().isOk()).
			andExpect(model().attributeExists("user")).
			andExpect(model().attributeExists("fruits")).
			andExpect(MockMvcResultMatchers.model().attribute("user", hasProperty("email", equalTo("other@email.com")))).
			andExpect(view().name("profileEditForm"));
	}
	
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canEditUser() throws Exception {
		
		mvc.perform(get("/profile/edit/"+otherUser.getId())).
		andExpect(status().isOk()).
		andExpect(model().attributeExists("user")).
		andExpect(model().attributeExists("fruits")).
		andExpect(MockMvcResultMatchers.model().attribute("user", hasProperty("email", equalTo("other@email.com")))).
		andExpect(view().name("profileEditForm"));
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
	    multiValueMap.add("id", Long.toString(otherUser.getId()));
	    multiValueMap.add("name", "newName");
	    multiValueMap.add("address", otherUser.getAddress());
	    multiValueMap.add("email", otherUser.getEmail());
	    multiValueMap.add("phoneNum", otherUser.getPhoneNum());
	    multiValueMap.add("password", "newPass");
	    
	    String[] fruitList = {"Cherry"};
	    MockMultipartFile file = new MockMultipartFile(
	            "image", 
	            "", 
	            MediaType.TEXT_PLAIN_VALUE, 
	            "Hello, World!".getBytes()
	          );;
		
		mvc.perform(multipart("/profile/edit/save").file(file).params(multiValueMap).param("idf", fruitList)).
			andExpect(status().isFound()).
			andExpect(view().name("redirect:/profile"));
		
		assertEquals("newName",userService.getById(otherUser.getId()).getName());
		assertEquals("Cherry",fruitService.getByProdID(otherUser.getId()).get(0).getType());
		
	}
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canFollowUser() throws Exception {
		mvc.perform(get("/profile/"+otherUser.getId())).
		andExpect(status().isOk());
	
		mvc.perform(get("/profile/follow"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:/profile/"+otherUser.getId()));
	
		assertEquals(1,followerService.getAllFollowers(otherUser.getId()).size());
	}
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canGetListOfFollowedUsers() throws Exception {
		mvc.perform(get("/profile/followerlist"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("followerList"))
			.andExpect(view().name("followerList"));
		
	}
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canUnFollowUser() throws Exception {
		mvc.perform(get("/profile/"+otherUser.getId())).
		andExpect(status().isOk());
	
		mvc.perform(get("/profile/unfollow"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:/profile/"+otherUser.getId()));
	
		assertEquals(0,followerService.getAllFollowers(otherUser.getId()).size());
	}
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canGetRatingForm() throws Exception{
		
		mvc.perform(get("/profile/rate/"+otherUser.getId())).
			andExpect(status().isOk()).
			andExpect(model().attributeExists("id")).
			andExpect(model().attributeExists("user")).
			andExpect(model().attribute("id",equalTo(otherUser.getId()))).
			andExpect(view().name("rateUser"));
	}
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canSaveRating() throws Exception{
		
		mvc.perform(get("/profile/rate/"+otherUser.getId())).
			andExpect(status().isOk()).
			andExpect(model().attributeExists("id")).
			andExpect(model().attributeExists("user")).
			andExpect(model().attribute("id",equalTo(otherUser.getId()))).
			andExpect(view().name("rateUser"));
		
		mvc.perform(post("/profile/rate/save").param("rating", "3"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:/profile/"+otherUser.getId()));
		
		assertEquals(3f,ratingService.getSpecificUserRating(otherUser.getId(), loggedInUser.getId()).getRating());
		assertEquals(3f,userService.getById(otherUser.getId()).getRating());
	}
	
	@Test
	@WithUserDetails(value="email@email.com")
	void canUpdateRating() throws Exception{
		
		mvc.perform(get("/profile/rate/"+otherUser.getId())).
			andExpect(status().isOk()).
			andExpect(model().attributeExists("id")).
			andExpect(model().attributeExists("user")).
			andExpect(model().attribute("id",equalTo(otherUser.getId()))).
			andExpect(view().name("rateUser"));
		
		mvc.perform(post("/profile/rate/save").param("rating", "2"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:/profile/"+otherUser.getId()));
		
		assertEquals(2f,ratingService.getSpecificUserRating(otherUser.getId(), loggedInUser.getId()).getRating());
		assertEquals(2f,userService.getById(otherUser.getId()).getRating());
	}
	
	
	
	
	
	
}
