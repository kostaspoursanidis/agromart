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

import com.Model.CartItem;
import com.Repos.CartItemRepo;

@DataJpaTest
public class CartItemRepoTest {
	
	@Autowired 
	private CartItemRepo cartItemRepoTest;
	
	private List<CartItem> expected = new ArrayList<>();
	
	private Long buyer_id = (long) 1;
	private Long producer_id = (long) 2;
	private String status_sent = "sent";
	private String status_pending = "pending";
	
	@BeforeEach
	void setUp() {
		CartItem ci1 = new CartItem();
		CartItem ci2 = new CartItem();
		ci1.setBuyer_id(buyer_id);
		ci2.setBuyer_id(buyer_id);
		ci1.setProducer_id(producer_id);
		ci2.setProducer_id(producer_id);
		ci1.setStatus(status_pending);
		ci2.setStatus(status_sent);
		cartItemRepoTest.save(ci1);
		cartItemRepoTest.save(ci2);
		
		expected.add(ci1);
		expected.add(ci2);
		
	}
	
	@AfterEach
    void tearDown() {
        cartItemRepoTest.deleteAll();
        expected.clear();
    }
	
	@Test
	void getCurrentUserCartItems() {
		List<CartItem> querried = cartItemRepoTest.getCurrentUserCI(buyer_id);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getBuyer_id(),querried.get(0).getBuyer_id());
		assertEquals(expected.get(0).getStatus(),querried.get(0).getStatus());
	}
	
	@Test
	void getCurrentUserProducerClaimedItems() {
		List<CartItem> querried = cartItemRepoTest.getCurrentUserCIProd(producer_id);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(1).getProducer_id(),querried.get(0).getProducer_id());
		assertEquals(expected.get(1).getStatus(),querried.get(0).getStatus());
	}
	
	

}
