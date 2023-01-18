package com.repoTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.Model.Fruits;
import com.Repos.FruitRepo;

@DataJpaTest
public class FruitRepoTest {

	
	@Autowired 
	private FruitRepo fruitRepoTest;
	
	private List<Fruits> expected = new ArrayList<>();
	
	private Long producer_id = (long) 1;
	private String type1 = "Cherry";
	private String type2 = "Pear";

	
	@BeforeEach
	void setUp() {
		Fruits f1 = new Fruits();
		Fruits f2 = new Fruits();
	
		f1.setProd_id(producer_id);
		f1.setType(type1);
		f2.setProd_id(producer_id);
		f2.setType(type2);
		
		fruitRepoTest.save(f1);
		fruitRepoTest.save(f2);
		
		expected.add(f1);
		expected.add(f2);
		
	}
	
	@AfterEach
    void tearDown() {
		fruitRepoTest.deleteAll();
        expected.clear();
    }
	
	@Test
	void deleteAllFruitsFromProducer() {
		fruitRepoTest.deleteByProdID(producer_id);
		
		List<Fruits> querried = fruitRepoTest.getByProdID(producer_id);
		
		assertThat(querried).isNotNull();
		assertEquals(querried.size(),0);
	}
	
	@Test
	void getAllProducersFruits() {
		List<Fruits> querried = fruitRepoTest.getByProdID(producer_id);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getProd_id(),querried.get(0).getProd_id());
		assertEquals(expected.get(0).getType(),querried.get(0).getType());
		assertEquals(expected.get(1).getProd_id(),querried.get(1).getProd_id());
		assertEquals(expected.get(1).getType(),querried.get(1).getType());
	}
	
	@Test
	void getSpecificFruitOfProducer() {
		Fruits querried = fruitRepoTest.hasFruit(producer_id, type1);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getProd_id(),querried.getProd_id());
		assertEquals(expected.get(0).getType(),querried.getType());
		
	}
}
