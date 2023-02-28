package com.repoTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.model.BuyerOrder;
import com.repos.BuyerOrderRepo;

@DataJpaTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
public class BuyerOrderRepoTest {

	@Autowired
	private BuyerOrderRepo buyerOfferRepoTest;
	
	private Long buyer_id = (long) 5;
	private Long producer_id = (long) 6;
	private String type_of_product = "Pear";
	
	private List<BuyerOrder> expected = new ArrayList<>();
	
	
	
	@BeforeEach
    void setUp() {
        BuyerOrder bo = new BuyerOrder();
        bo.setBuyer_id(buyer_id);
        bo.setProducer_id(producer_id);
        bo.setType_of_product(type_of_product);
        buyerOfferRepoTest.save(bo);
        expected.add(bo);
    }
	
	@AfterEach
    void tearDown() {
        buyerOfferRepoTest.deleteAll();
        expected.clear();
    }
	
	@Test
	void getCurrentUserBO() {
		List<BuyerOrder> querried = buyerOfferRepoTest.getCurrentUserBO(buyer_id);
		
		assertThat(querried).isNotNull();
		assertThat( expected.size()).isEqualTo(querried.size());
		assertEquals(expected.get(0).getBuyer_id(),querried.get(0).getBuyer_id());	
	}
	
	@Test
	void getCurrentUserBOProd() {
		List<BuyerOrder> querried = buyerOfferRepoTest.getCurrentUserBOProd(producer_id);
		
		assertThat(querried).isNotNull();
		assertThat( expected.size()).isEqualTo(querried.size());
		assertEquals(expected.get(0).getProducer_id(),querried.get(0).getProducer_id());	
	}
	
	@Test
	void getFilteredBO() {
		List<BuyerOrder> querried = buyerOfferRepoTest.getFilteredBO(type_of_product);
		
		assertThat(querried).isNotNull();
		assertThat( expected.size()).isEqualTo(querried.size());
		assertEquals(expected.get(0).getType_of_product(),querried.get(0).getType_of_product());	
	}
	
	
	
	
	
	
	
	
	
}
