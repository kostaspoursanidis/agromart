package com.controllerTests;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.Model.CartItem;
import com.Model.RetailOffer;
import com.Model.User;
import com.Repos.CartItemRepo;
import com.Repos.RetailOfferRepo;
import com.Repos.UserRepo;
import com.Services.RetailOfferService;
import com.Services.UserService;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
public class ShoppingCartControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemRepo cartRepo;
	
	@Autowired
	private RetailOfferRepo coRepo;
	
	@Autowired
	private RetailOfferService coService;
	
	private User producerUser;
	private User retailUser;
	private RetailOffer cOffer;
	
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
			
		retailUser = getMockUserByRole("civilian");
		retailUser.setEmail("other@email.com");
		userService.save(retailUser);
		
		cOffer = new RetailOffer(1f, userService.getById(producerUser.getId()).getId(), "type","text","title",null);
		coService.saveOffer(cOffer);
	}
	 
	 @AfterAll
	 void tearDown() {
	    userRepo.deleteAll();
	    cartRepo.deleteAll();
	    coRepo.deleteAll();
	    	
	 }
	 

	 @Test
	 @WithUserDetails(value="other@email.com")
	 void canGetAddToCartPage() throws Exception {
		 mvc.perform(get("/cart/create/"+cOffer.getId()))
		 	.andExpect(status().isOk())
		 	.andExpect(model().attributeExists("item"))
		 	.andExpect(view().name("addToCart"));
	 }
	 

	 @Test
	 @WithUserDetails(value="other@email.com")
	 void canSaveCartItem() throws Exception {
		 String producer_id = Long.toString(producerUser.getId());
		 String co_id = Long.toString(cOffer.getId());
		 
		 MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		 multiValueMap.add("id", "1");
		 multiValueMap.add("producer_id", producer_id);
		 multiValueMap.add("civil_offer_id", co_id);
		 multiValueMap.add("kilos_wanted", "1");
		 
		 
		 mvc.perform(post("/cart/create/save").params(multiValueMap))
		 	.andExpect(status().isFound())
		 	.andExpect(view().name("redirect:/homepage"));
		 
		 assertEquals(cartRepo.count(),1);
	 }
	 

	 @Test
	 @WithUserDetails(value="other@email.com")
	 void canGetCartManagePage() throws Exception {
		 mvc.perform(get("/cart"))
		 	.andExpect(status().isOk())
		 	.andExpect(model().attributeExists("user"))
		 	.andExpect(model().attributeExists("totalPrice"))
		 	.andExpect(model().attributeExists("userCIs"))
		 	.andExpect(model().attributeExists("cOffers"))
		 	.andExpect(view().name("cart"));
	 }
	 

	 @Test
	 @WithUserDetails(value="other@email.com")
	 void canGetEditPage() throws Exception {
		String item_id = Long.toString(cartRepo.findAll().get(0).getId());
		 
		 mvc.perform(get("/cart/edit/"+item_id))
		 	.andExpect(status().isOk())
		 	.andExpect(model().attributeExists("item"))
		 	.andExpect(view().name("addToCart"));
	 }
	 

	 @Test
	 @WithUserDetails(value="other@email.com")
	 void canEditItem() throws Exception {
		String item_id = Long.toString(cartRepo.findAll().get(0).getId());
		String producer_id = Long.toString(producerUser.getId());
		String co_id = Long.toString(cOffer.getId());
		 
		 mvc.perform(get("/cart/edit/"+item_id))
		 	.andExpect(status().isOk())
		 	.andExpect(model().attributeExists("item"))
		 	.andExpect(view().name("addToCart"));
		 
		 MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		 multiValueMap.add("id", item_id);
		 multiValueMap.add("producer_id", producer_id);
		 multiValueMap.add("civil_offer_id", co_id);
		 multiValueMap.add("kilos_wanted", "2");
		 
		 mvc.perform(post("/cart/create/save").params(multiValueMap))
		 	.andExpect(status().isFound())
		 	.andExpect(view().name("redirect:/homepage"));
		 
		 assertEquals(2f,cartRepo.findAll().get(0).getKilos_wanted());

	 }
	 

	 @Test
	 @WithUserDetails(value="other@email.com")
	 void canCheckoutItemsInCart() throws Exception {
		 
		 mvc.perform(get("/cart/checkout"))
		 	.andExpect(status().isFound())
		 	.andExpect(view().name("redirect:/homepage"));
		 
		 assertEquals("sent",cartRepo.findAll().get(0).getStatus());
	 }
	 

	 @Test
	 @WithUserDetails(value="other@email.com")
	 void canGetMyOrdersPage() throws Exception {
		 
		 mvc.perform(get("/cart/myorders"))
		 	.andExpect(status().isOk())
		 	.andExpect(model().attributeExists("claimedList"))
		 	.andExpect(model().attributeExists("types"))
		 	.andExpect(model().attributeExists("prices"))
		 	.andExpect(model().attributeExists("addresses"))
		 	.andExpect(view().name("myOrders"));
		 
	 }
	 

	 @Test
	 @WithUserDetails(value="other@email.com")
	 void canDeleteItemFromCart() throws Exception {
		CartItem dummy = new CartItem();
		cartRepo.save(dummy);
		String item_id = Long.toString(cartRepo.findAll().get(1).getId());
		 
		 mvc.perform(get("/cart/delete/"+item_id))
		 	.andExpect(status().isFound())
		 	.andExpect(view().name("redirect:/cart"));
		 
		 assertEquals(1,cartRepo.count());
		 
	 }
	 
	 
	 
	 
	 
}
