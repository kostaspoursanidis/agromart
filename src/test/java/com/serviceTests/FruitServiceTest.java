package com.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.model.Fruit;
import com.repos.FruitRepo;
import com.services.FruitService;

@ExtendWith(MockitoExtension.class)
public class FruitServiceTest {
	
	@Mock
	private FruitRepo fruitRepo;
	
	@InjectMocks
	private FruitService serviceUnderTest;
	
	@Test
	void canDeleteAllFruitsFromSpecificProducer() {
		
		Long producers_id = 1l;
		String fruit_type = "type" ;
		
		Fruit expected = new Fruit();
		expected.setProd_id(producers_id);
		expected.setType(fruit_type);
		
		
		
		serviceUnderTest.deleteByProdID(producers_id);
		
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		
		verify(fruitRepo).deleteByProdID(idArgumentCaptor.capture());
		
		Long capturedProducersId = idArgumentCaptor.getValue();
		
		assertEquals(capturedProducersId,expected.getProd_id());
		
	}
	
	@Test
	void canGetAllFruitsOfSpecificProducer() {
		Long producers_id = 1l;
		String fruit_type = "type" ;
		
		Fruit expected = new Fruit();
		expected.setProd_id(producers_id);
		expected.setType(fruit_type);
		
		List<Fruit> expected_list = new ArrayList<>();
		expected_list.add(expected);
		
		Mockito.when(fruitRepo.getByProdID(producers_id)).thenReturn(expected_list);
	
		List<Fruit> returned_list = serviceUnderTest.getByProdID(producers_id);
		
		assertEquals(returned_list.get(0).getProd_id(),expected_list.get(0).getProd_id());
	
	
	
	}
	
	@Test
	void hasFruitShouldReturnTrueIfFruitExistsForSpecificUser() {
		Long producers_id = 1l;
		String fruit_type = "type" ;
		
		Fruit expected = new Fruit();
		expected.setProd_id(producers_id);
		expected.setType(fruit_type);
		
		Mockito.when(fruitRepo.hasFruit(producers_id,fruit_type)).thenReturn(expected);
		
		Boolean returned = serviceUnderTest.hasFruit(producers_id, fruit_type);
		
		assertThat(returned).isEqualTo(true);

		
	}
	
	@Test
	void hasFruitShouldReturnFalseIfFruitNotExistsForSpecificUser() {
		Long producers_id = 1l;
		String fruit_type = "type" ;
		
		Fruit expected = new Fruit();
		expected.setProd_id(producers_id);
		expected.setType(fruit_type);
		
		Mockito.when(fruitRepo.hasFruit(producers_id,fruit_type)).thenReturn(null);
		
		Boolean returned = serviceUnderTest.hasFruit(producers_id, fruit_type);
		
		assertThat(returned).isEqualTo(false);

		
	}
		
	@Test
	void canSaveFruitToUser() {
		Long producers_id = 1l;
		String fruit_type = "type" ;
		
		Fruit expected = new Fruit();
		expected.setProd_id(producers_id);
		expected.setType(fruit_type);
		
		serviceUnderTest.saveFruitToUser(expected);
		
		ArgumentCaptor<Fruit> fruitArgumentCaptor = ArgumentCaptor.forClass(Fruit.class);
		
		verify(fruitRepo).save(fruitArgumentCaptor.capture());
		
		Fruit capturedFruit = fruitArgumentCaptor.getValue();
		
		assertEquals(capturedFruit.getProd_id(),expected.getProd_id());
		assertEquals(capturedFruit.getType(),expected.getType());

	}

}
