package com.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.model.CartItem;
import com.model.User;
import com.repos.CartItemRepo;
import com.repos.RetailOfferRepo;
import com.repos.UserRepo;
import com.services.UserService;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(OrderAnnotation.class)
public class RetailOfferControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RetailOfferRepo coRepo;
	
	@Autowired
	private CartItemRepo cartItemRepo;
	
	@Autowired
	private UserService userService;
	
	private User loggedInUser;
	
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
		loggedInUser = getMockUserByRole("producer");
		userService.save(loggedInUser);
		
	}
    
    @AfterAll
    void tearDown() {
    	userRepo.deleteAll();
    	coRepo.deleteAll();
    	cartItemRepo.deleteAll();
    	
    }
    
    @Test
	@WithUserDetails(value="email@email.com")
	void canGetRetailOfferCreatePage() throws Exception {
    	mvc.perform(get("/CivilOffers/create"))
    		.andExpect(status().isOk())
    		.andExpect(model().attributeExists("civiloffer"))
    		.andExpect(view().name("civilOfferCreationPage"));
    }
    
    @Order(1)
    @Test
	@WithUserDetails(value="email@email.com")
	void canCreateRetailOffer() throws Exception {
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
	    multiValueMap.add("id", "1");
	    multiValueMap.add("price_per_kg", "1");
	    multiValueMap.add("producer_id", Long.toString(loggedInUser.getId()));
	    multiValueMap.add("type_of_product", "Cherry");
	    multiValueMap.add("offer_text", "offer_text");
	    multiValueMap.add("title", "title");
	    multiValueMap.add("photo", "none");
	    
	    
	    MockMultipartFile file = new MockMultipartFile(
	            "image", 
	            "", 
	            MediaType.TEXT_PLAIN_VALUE, 
	            "Hello, World!".getBytes()
	          );;
		
		mvc.perform(multipart("/CivilOffers/create/save").file(file).params(multiValueMap).param("other", "none")).
			andExpect(status().isFound()).
			andExpect(view().name("redirect:/CivilOffers"));
		
		assertEquals(coRepo.findAll().size(),1);
		coRepo.deleteAll();
		
	}
    
    @Order(2)
    @Test
	@WithUserDetails(value="email@email.com")
	void canCreateRetailOfferWithProductTypeOther() throws Exception {
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
	    multiValueMap.add("id", "1");
	    multiValueMap.add("price_per_kg", "1");
	    multiValueMap.add("producer_id", Long.toString(loggedInUser.getId()));
	    multiValueMap.add("type_of_product", "Other");
	    multiValueMap.add("offer_text", "offer_text");
	    multiValueMap.add("title", "title");
	    multiValueMap.add("photo", "none");
	    
	    
	    MockMultipartFile file = new MockMultipartFile(
	            "image", 
	            "", 
	            MediaType.TEXT_PLAIN_VALUE, 
	            "Hello, World!".getBytes()
	          );;
		
		mvc.perform(multipart("/CivilOffers/create/save").file(file).params(multiValueMap).param("other", "something")).
			andExpect(status().isFound()).
			andExpect(view().name("redirect:/CivilOffers"));
		
		assertEquals(coRepo.findAll().size(),1);
		
	}
    
    @Order(3)
    @Test
	@WithUserDetails(value="email@email.com")
	void canGetRetailOfferEditPage() throws Exception {
    	String co_id = Long.toString(coRepo.findAll().get(0).getId());
    	
    	mvc.perform(get("/CivilOffers/edit/"+co_id))
    		.andExpect(status().isOk())
    		.andExpect(model().attributeExists("civiloffer"))
    		.andExpect(view().name("civilOfferCreationPage"));
    }
    
    @Order(4)
    @Test
	@WithUserDetails(value="email@email.com")
	void canGetRetailOrdersListPage() throws Exception {
    	
    	mvc.perform(get("/CivilOffers/claimedOffers"))
    		.andExpect(status().isOk())
    		.andExpect(model().attributeExists("claimedList"))
    		.andExpect(model().attributeExists("types"))
    		.andExpect(model().attributeExists("prices"))
    		.andExpect(model().attributeExists("addresses"))
    		.andExpect(view().name("claimed"));
    }
    
    @Order(5)
    @Test
	@WithUserDetails(value="email@email.com")
	void canSetRetailOrderAsCompleted() throws Exception {
    	CartItem temp_item = new CartItem();
    	temp_item.setBuyer_id(userService.getAllUsers().get(0).getId());
    	cartItemRepo.save(temp_item);
    	String ci_id = Long.toString(cartItemRepo.findAll().get(0).getId());
    	
    	
    	mvc.perform(get("/CivilOffers/completed/"+ci_id))
    		.andExpect(status().isFound())
    		.andExpect(view().name("redirect:/CivilOffers/claimedOffers"));
    
    	assertEquals(cartItemRepo.findAll().get(0).getStatus(),"completed");
    }
    
    
    
}
