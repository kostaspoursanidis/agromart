package com.controllerTests;

//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatsControllerTest {
	
	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@BeforeAll
	void setUp() {
		mvc = MockMvcBuilders
	            .webAppContextSetup(context)
	            //.apply(springSecurity())
	            .build();
	}
	
	@Test
	//@WithMockUser(value = "producer" ,roles={"PRODUCER"})
	void canGetStatsPage() throws Exception {
		
		mvc.perform(get("/stats")).
		andExpect(status().isOk()).
		andExpect(model().attributeExists("avgArray")).
		andExpect(model().attributeExists("avgVArray")).
		andExpect(model().attributeExists("avgWArray")).
		andExpect(model().attributeExists("avgVWArray")).
		andExpect(view().name("statsPage"));	
	
	}
}
