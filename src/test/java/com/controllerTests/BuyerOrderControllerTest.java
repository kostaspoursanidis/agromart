package com.controllerTests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import com.Model.BuyerOrder;
import com.Model.User;
import com.Repos.BuyerOrderRepo;
import com.Repos.UserRepo;
import com.Services.UserService;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(OrderAnnotation.class)
public class BuyerOrderControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private BuyerOrderRepo boRepo;
	
	@Autowired
	private UserService userService;
	
	private User producerUser;
	private User buyerUser;
	
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
		producerUser = getMockUserByRole("producer");
		userService.save(producerUser);
		
		buyerUser = getMockUserByRole("buyer");
		buyerUser.setEmail("buyer@email.com");
		userService.save(buyerUser);
		
	}
    
    @AfterAll
    void tearDown() {
    	userRepo.deleteAll();
    	boRepo.deleteAll();
    	
    }
    
    @Order(1)
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetManageBuyerOffersPageForProducers() throws Exception {
    	mvc.perform(get("/BuyerOffers")).
		andExpect(status().isOk()).
		andExpect(model().attributeExists("user")).
		andExpect(model().attributeExists("userBOs")).
		andExpect(model().attribute("user", hasProperty("email", equalTo("email@email.com")))).
		andExpect(model().attribute("user", hasProperty("role", equalTo("producer")))).
		andExpect(view().name("buyerOffers"));
    }
    
    @Order(2)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canGetManageBuyerOffersPageForBuyers() throws Exception {
    	mvc.perform(get("/BuyerOffers")).
		andExpect(status().isOk()).
		andExpect(model().attributeExists("user")).
		andExpect(model().attributeExists("userBOs")).
		andExpect(model().attribute("user", hasProperty("email", equalTo("buyer@email.com")))).
		andExpect(model().attribute("user", hasProperty("role", equalTo("buyer")))).
		andExpect(view().name("buyerOffers"));
    }
    
    @Order(3)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canGetCreateBuyerOfferPage() throws Exception {
    	String prod_id = Long.toString(userRepo.findAll().get(0).getId());
    	
    	mvc.perform(get("/BuyerOffers/create/"+prod_id)).
		andExpect(status().isOk()).
		andExpect(model().attributeExists("buyerOffer")).
		andExpect(view().name("buyerOfferCreation"));
    }
    
    @Order(4)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canCreateBuyerOfferWithType() throws Exception {
    	String prod_id = Long.toString(userRepo.findAll().get(0).getId());
    	
    	
    	MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
	    multiValueMap.add("id", "1");
	    multiValueMap.add("producer_id", prod_id);
	    multiValueMap.add("price_per_kg", "1");
	    multiValueMap.add("appros_kilos_wanted", "1");
	    multiValueMap.add("type_of_product", "Cherry");

    	
    	mvc.perform(post("/BuyerOffers/create/save").params(multiValueMap).param("other", "none")).
		andExpect(status().isFound()).
		andExpect(view().name("redirect:/BuyerOffers"));
    	
    	assertEquals(boRepo.findAll().size(),1);
    	boRepo.deleteAll();
    }
    
    @Order(5)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canCreateBuyerOfferWithTypeOther() throws Exception {
    	String prod_id = Long.toString(userRepo.findAll().get(0).getId());
    	
    	
    	MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
	    multiValueMap.add("id", "1");
	    multiValueMap.add("producer_id", prod_id);
	    multiValueMap.add("price_per_kg", "1");
	    multiValueMap.add("appros_kilos_wanted", "1");
	    multiValueMap.add("type_of_product", "Other");

    	
    	mvc.perform(post("/BuyerOffers/create/save").params(multiValueMap).param("other", "something")).
		andExpect(status().isFound()).
		andExpect(view().name("redirect:/BuyerOffers"));
    	
    	assertEquals(boRepo.findAll().size(),1);
    }
    
    @Order(6)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canGetEditBuyerOfferPage() throws Exception {
    	String bo_id = Long.toString(boRepo.findAll().get(0).getId());
    	
    	mvc.perform(get("/BuyerOffers/edit/"+bo_id)).
		andExpect(status().isOk()).
		andExpect(model().attributeExists("buyerOffer")).
		andExpect(view().name("buyerOfferCreation"));
    }
    
    @Order(7)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canEditBuyerOffer() throws Exception {
    	String bo_id = Long.toString(boRepo.findAll().get(0).getId());
    	String prod_id = Long.toString(userRepo.findAll().get(0).getId());
    	
    	
    	MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
	    multiValueMap.add("id", bo_id);
	    multiValueMap.add("producer_id", prod_id);
	    multiValueMap.add("price_per_kg", "10");
	    multiValueMap.add("appros_kilos_wanted", "10");
	    multiValueMap.add("type_of_product", "Pear");

	    mvc.perform(get("/BuyerOffers/edit/"+bo_id)).
		andExpect(status().isOk()).
		andExpect(model().attributeExists("buyerOffer")).
		andExpect(view().name("buyerOfferCreation"));
    	
	    mvc.perform(post("/BuyerOffers/create/save").params(multiValueMap).param("other", "something")).
		andExpect(status().isFound()).
		andExpect(view().name("redirect:/BuyerOffers"));
    	
    	assertEquals(boRepo.findAll().size(),1);
    	
    }
    
    @Order(8)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canAcceptBuyerOfferAsProducer() throws Exception {
    	String bo_id = Long.toString(boRepo.findAll().get(0).getId());
    	
    	mvc.perform(get("/BuyerOffers/accept/"+bo_id)).
		andExpect(status().isFound()).
		andExpect(view().name("redirect:/BuyerOffers"));
    	
    	assertEquals(boRepo.findAll().get(0).getStatus(),"pending_payment");
    }
    
    @Order(9)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canGetDeclineBuyerOfferForm() throws Exception {
    	BuyerOrder temp_bo = new BuyerOrder();
    	boRepo.save(temp_bo);
    	String bo_id = Long.toString(temp_bo.getId());
    	
    	mvc.perform(get("/BuyerOffers/decline/"+bo_id)).
		andExpect(status().isOk()).
		andExpect(model().attributeExists("order")).
		andExpect(view().name("declineForm"));
    	
    	boRepo.deleteById(temp_bo.getId());
    }
    
    @Order(10)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canDeclineBuyerOfferAsProducer() throws Exception {
    	BuyerOrder temp_bo = new BuyerOrder();
    	boRepo.save(temp_bo);
    	String bo_id = Long.toString(temp_bo.getId());
    	
    	MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
	    multiValueMap.add("id", bo_id);

    	mvc.perform(post("/BuyerOffers/decline/save").params(multiValueMap).param("reason", "some_reason")).
		andExpect(status().isFound()).
		andExpect(view().name("redirect:/BuyerOffers"));
    	
    	assertEquals(boRepo.count(),1);
    }
    
    @Order(11)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canMakeDepositAsBuyer() throws Exception {
    	String bo_id = Long.toString(boRepo.findAll().get(0).getId());
    	
    	mvc.perform(get("/BuyerOffers/depositMade/"+bo_id)).
		andExpect(status().isFound()).
		andExpect(view().name("redirect:/BuyerOffers"));
    	
    	assertEquals(boRepo.findAll().get(0).getStatus(),"awaiting_completion");
    }
    
    @Order(12)
    @Test
    @WithUserDetails(value="buyer@email.com")
    void canConfirmDepositAsProducer() throws Exception {
    	String bo_id = Long.toString(boRepo.findAll().get(0).getId());
    	
    	mvc.perform(get("/BuyerOffers/confirmDeposit/"+bo_id)).
		andExpect(status().isFound()).
		andExpect(view().name("redirect:/BuyerOffers"));
    	
    	assertEquals(boRepo.findAll().get(0).getStatus(),"completed");
    }
    
    
    
    
    
}
