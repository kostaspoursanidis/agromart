package com.repoTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.model.RetailOffer;
import com.repos.RetailOfferRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
public class RetailOfferRepoTest {
	
	@Autowired
	private RetailOfferRepo coRepoTest;
	
	private Float maxPricePerKg = 1.2f;
	private Long producer_id = (long) 1;
	private Long producer_not_exists = (long) 2;
	private String type1 = "Cherry";
	private String type2 = "Pear";
	private String type_not_exists = "not_exists";
	private List<RetailOffer> expected = new ArrayList<>();

	@BeforeEach
	void setUp() {
		RetailOffer co1 = new RetailOffer();
		RetailOffer co2 = new RetailOffer();
		
		//co1 setup
		co1.setProducer_id(producer_id);
		co1.setPrice_per_kg(1.1f);
		co1.setType_of_product(type1);
		
		//co2 setup
		co2.setProducer_id(producer_id);
		co2.setPrice_per_kg(1.3f);
		co2.setType_of_product(type2);
		
		coRepoTest.save(co1);
		coRepoTest.save(co2);
		
		expected.add(co1);
		expected.add(co2);		
	}
	
	@AfterEach
	void tearDown() {
		expected.clear();
		coRepoTest.deleteAll();
	}
		
	@Test
	void getROByProdID() {
		
		List<RetailOffer> querried = coRepoTest.getCurrentUserCO(producer_id);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getProducer_id(),querried.get(0).getProducer_id());
		assertEquals(expected.get(1).getProducer_id(),querried.get(1).getProducer_id());
	}
	
	@Test
	void getROFilteredByProdType() {
		
		List<RetailOffer> querried = coRepoTest.getFilteredCO(type1);
		
		assertThat(querried).isNotNull();
		assertNotEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getType_of_product(),querried.get(0).getType_of_product());
		
	}
	
	@Test
	void getROFilteredByProdTypeAndMaxprice() {
		
		List<RetailOffer> querried = coRepoTest.getFilteredCOMax(type1,maxPricePerKg);
		
		assertThat(querried).isNotNull();
		assertNotEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getType_of_product(),querried.get(0).getType_of_product());
		assertEquals(expected.get(0).getPrice_per_kg(),querried.get(0).getPrice_per_kg());
		
	}
	
	@Test
	void getROFilteredByProdTypeAscendingOrder() {
		
		List<RetailOffer> querried = coRepoTest.getFilteredCOAsc(type1);
		
		assertThat(querried).isNotNull();
		assertNotEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getType_of_product(),querried.get(0).getType_of_product());
		assertEquals(expected.get(0).getPrice_per_kg(),querried.get(0).getPrice_per_kg());
		
	}
	
	@Test
	void getROFilteredByProdTypeDescendingOrder() {
		
		List<RetailOffer> querried = coRepoTest.getFilteredCODesc(type2);
		
		assertThat(querried).isNotNull();
		assertNotEquals(expected.size(),querried.size());
		assertEquals(expected.get(1).getType_of_product(),querried.get(0).getType_of_product());
		assertEquals(expected.get(1).getPrice_per_kg(),querried.get(0).getPrice_per_kg());
		
	}
	
	@Test
	void getROFilteredByProdTypeAndAscendingOrderAndMaxprice() {
		
		List<RetailOffer> querried = coRepoTest.getFilteredCOMaxAsc(type1,maxPricePerKg);
		
		assertThat(querried).isNotNull();
		assertNotEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getType_of_product(),querried.get(0).getType_of_product());
		assertEquals(expected.get(0).getPrice_per_kg(),querried.get(0).getPrice_per_kg());
		
	}
	
	@Test
	void getROFilteredByProdTypeAndDescendingOrderAndMaxprice() {
		
		List<RetailOffer> querried = coRepoTest.getFilteredCOMaxDesc(type1,maxPricePerKg);
		
		assertThat(querried).isNotNull();
		assertNotEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getType_of_product(),querried.get(0).getType_of_product());
		assertEquals(expected.get(0).getPrice_per_kg(),querried.get(0).getPrice_per_kg());
	}
	
	@Test
	void getROFilteredByProdTypeAndDescendingOrderAndMaxpriceButTypeNotExists() {
		
		List<RetailOffer> querried = coRepoTest.getFilteredCOMaxDesc(type_not_exists,maxPricePerKg);
		
		assertThat(querried).isNotNull();
		assertEquals(querried.size(),0);
	}	
	
	@Test
	void getROByProdIDButIDNotExists() {
		
		List<RetailOffer> querried = coRepoTest.getCurrentUserCO(producer_not_exists);
		
		assertThat(querried).isNotNull();
		assertEquals(querried.size(),0);
	}
	
	

}
