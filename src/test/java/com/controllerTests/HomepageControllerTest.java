package com.controllerTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.*;

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

import com.model.Fruit;
import com.model.RetailOffer;
import com.model.User;
import com.repos.FruitRepo;
import com.repos.RetailOfferRepo;
import com.repos.UserRepo;
import com.services.UserService;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
public class HomepageControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RetailOfferRepo coRepo;
	
	@Autowired
	private FruitRepo fruitRepo;
	
	//@Spy
	//private CivilOfferService coService;
	
	@Autowired
	private UserService userService;
	
	
	private User producerUser;
    private User buyerUser;
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
		user.setRating(4f);
							
		return user;
	}
	
	@BeforeAll
	void setUp() throws Exception {

		
		producerUser = getMockUserByRole("producer");
		userService.save(producerUser);
		
		Fruit fruit = new Fruit();
		fruit.setType("type");
		fruit.setProd_id(producerUser.getId());
		fruitRepo.save(fruit);
		
		buyerUser = getMockUserByRole("buyer");
		buyerUser.setEmail("other@email.com");
		userService.save(buyerUser);
		
		cOffer = new RetailOffer(1f, producerUser.getId(), "type","text","title",null);
		coRepo.save(cOffer);
		
	}
    
    @AfterAll
    void tearDown() {
    	userRepo.deleteAll();
    	fruitRepo.deleteAll();
    	coRepo.deleteAll();
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetHomepage() throws Exception{
    	
    	mvc.perform(get("/homepage"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("offer"))
		.andExpect(model().attributeExists("userService"))
		.andExpect(view().name("homepage"));
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredHomepageWithAnyTypeProduct() throws Exception{
    	
    	String sortType = null;
    	String maxPrice = null;
    	
    	mvc.perform(get("/homepage/filtered").param("filter", "any").param("sorting", sortType).param("maxPrice",maxPrice))
		.andExpect(status().isOk())
		.andExpect(model().attribute("offer",hasSize(1)))
		.andExpect(model().attribute("offer",hasItem(allOf(hasProperty("type_of_product",is("type"))))))
		.andExpect(model().attributeExists("userService"))
		.andExpect(view().name("homepage"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredHomepageWithSpecificTypeProduct() throws Exception{
    	
    	String sortType = null;
    	String maxPrice = null;
    	
    	mvc.perform(get("/homepage/filtered").param("filter", "type").param("sorting", sortType).param("maxPrice",maxPrice))
		.andExpect(status().isOk())
		.andExpect(model().attribute("offer",hasSize(1)))
		.andExpect(model().attribute("offer",hasItem(allOf(hasProperty("type_of_product",is("type"))))))
		.andExpect(model().attributeExists("userService"))
		.andExpect(view().name("homepage"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredHomepageWithTypeAndSort() throws Exception{
    	
    	String sortType = "ascending";
    	String maxPrice = null;
    	
    	mvc.perform(get("/homepage/filtered").param("filter", "any").param("sorting", sortType).param("maxPrice",maxPrice))
		.andExpect(status().isOk())
		.andExpect(model().attribute("offer",hasSize(1)))
		.andExpect(model().attribute("offer",hasItem(allOf(hasProperty("type_of_product",is("type"))))))
		.andExpect(model().attributeExists("userService"))
		.andExpect(view().name("homepage"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredHomepageWithTypeAndMaxPrice() throws Exception{
    	
    	String sortType = null;
    	String maxPrice = "1.5f";
    	
    	mvc.perform(get("/homepage/filtered").param("filter", "any").param("sorting", sortType).param("maxPrice",maxPrice))
		.andExpect(status().isOk())
		.andExpect(model().attribute("offer",hasSize(1)))
		.andExpect(model().attribute("offer",hasItem(allOf(hasProperty("type_of_product",is("type"))))))
		.andExpect(model().attributeExists("userService"))
		.andExpect(view().name("homepage"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredHomepageWithTypeAndSortAndMaxPrice() throws Exception{
    	
    	String sortType = "ascending";
    	String maxPrice = "1.5f";
    	
    	mvc.perform(get("/homepage/filtered").param("filter", "any").param("sorting", sortType).param("maxPrice",maxPrice))
		.andExpect(status().isOk())
		.andExpect(model().attribute("offer",hasSize(1)))
		.andExpect(model().attribute("offer",hasItem(allOf(hasProperty("type_of_product",is("type"))))))
		.andExpect(model().attributeExists("userService"))
		.andExpect(view().name("homepage"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFindProducersPage() throws Exception{

    	
    	mvc.perform(get("/homepage/producers"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("fruitserv"))
		.andExpect(model().attributeExists("producers"))
		.andExpect(model().attributeExists("currUser"))
		.andExpect(view().name("findproducers"));
    	
    }
    
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredFindProducersPageWithAnyProductType() throws Exception{
    	
    	String rating = null;
    	String name = "";
    	
    	mvc.perform(get("/homepage/producers/filtered").param("produce", "any").param("rating", rating).param("name",name))
		.andExpect(status().isOk())
		.andExpect(model().attribute("producers",hasSize(1)))
		.andExpect(model().attribute("producers",hasItem(allOf(hasProperty("name",is("name"))))))
		.andExpect(model().attributeExists("fruitserv"))
		.andExpect(model().attributeExists("currUser"))
		.andExpect(view().name("findproducers"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredFindProducersPageWithSpecificProductType() throws Exception{
    	
    	String rating = null;
    	String name = "";
    	
    	mvc.perform(get("/homepage/producers/filtered").param("produce", "type").param("rating", rating).param("name",name))
		.andExpect(status().isOk())
		.andExpect(model().attribute("producers",hasSize(1)))
		.andExpect(model().attribute("producers",hasItem(allOf(hasProperty("name",is("name"))))))
		.andExpect(model().attributeExists("fruitserv"))
		.andExpect(model().attributeExists("currUser"))
		.andExpect(view().name("findproducers"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredFindProducersPageWithProductTypeAndRating() throws Exception{
    	
    	String rating = "3.5f";
    	String name = "";
    	
    	mvc.perform(get("/homepage/producers/filtered").param("produce", "type").param("rating", rating).param("name",name))
		.andExpect(status().isOk())
		.andExpect(model().attribute("producers",hasSize(1)))
		.andExpect(model().attribute("producers",hasItem(allOf(hasProperty("name",is("name"))))))
		.andExpect(model().attributeExists("fruitserv"))
		.andExpect(model().attributeExists("currUser"))
		.andExpect(view().name("findproducers"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredFindProducersPageWithSpecificProductTypeAndName() throws Exception{
    	
    	String rating = null;
    	String name = "name";
    	
    	mvc.perform(get("/homepage/producers/filtered").param("produce", "type").param("rating", rating).param("name",name))
		.andExpect(status().isOk())
		.andExpect(model().attribute("producers",hasSize(1)))
		.andExpect(model().attribute("producers",hasItem(allOf(hasProperty("name",is("name"))))))
		.andExpect(model().attributeExists("fruitserv"))
		.andExpect(model().attributeExists("currUser"))
		.andExpect(view().name("findproducers"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredFindProducersPageWithSpecificProductTypeAndNameAndRating() throws Exception{
    	
    	String rating = "3.5f";
    	String name = "name";
    	
    	mvc.perform(get("/homepage/producers/filtered").param("produce", "type").param("rating", rating).param("name",name))
		.andExpect(status().isOk())
		.andExpect(model().attribute("producers",hasSize(1)))
		.andExpect(model().attribute("producers",hasItem(allOf(hasProperty("name",is("name"))))))
		.andExpect(model().attributeExists("fruitserv"))
		.andExpect(model().attributeExists("currUser"))
		.andExpect(view().name("findproducers"));
    	
    }
    
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFindBuyersPage() throws Exception{

    	
    	mvc.perform(get("/homepage/buyers"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("buyers"))
		.andExpect(view().name("findbuyers"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredFindBuyersPageWithRating() throws Exception{
    	
    	String rating = "3.5f";
    	String name = "";
    	
    	mvc.perform(get("/homepage/buyers/filtered").param("rating", rating).param("name",name))
		.andExpect(status().isOk())
		.andExpect(model().attribute("buyers",hasSize(2)))
		.andExpect(model().attribute("buyers",hasItem(allOf(hasProperty("role",is("buyer"))))))
		.andExpect(view().name("findbuyers"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredFindBuyersPageWithName() throws Exception{
    	
    	String rating = null;
    	String name = "name";
    	
    	mvc.perform(get("/homepage/buyers/filtered").param("rating", rating).param("name",name))
		.andExpect(status().isOk())
		.andExpect(model().attribute("buyers",hasSize(2)))
		.andExpect(model().attribute("buyers",hasItem(allOf(hasProperty("role",is("buyer"))))))
		.andExpect(view().name("findbuyers"));
    	
    }
    
    @Test
    @WithUserDetails(value="email@email.com")
    void canGetFilteredFindBuyersPageWithRatingAndName() throws Exception{
    	
    	String rating = "3.5f";
    	String name = "name";
    	
    	mvc.perform(get("/homepage/buyers/filtered").param("rating", rating).param("name",name))
		.andExpect(status().isOk())
		.andExpect(model().attribute("buyers",hasSize(2)))
		.andExpect(model().attribute("buyers",hasItem(allOf(hasProperty("role",is("buyer"))))))
		.andExpect(view().name("findbuyers"));
    	
    }
    
    
	
	
	
}
