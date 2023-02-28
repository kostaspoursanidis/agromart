package com.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.model.RetailOffer;
import com.model.User;
import com.repos.RetailOfferRepo;
import com.services.RetailOfferService;
import com.services.UserService;

@ExtendWith(MockitoExtension.class)
public class RetailOfferServiceTest {
	
	@Mock
	private RetailOfferRepo coRepo;
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private RetailOfferService serviceUnderTest;
	
	@Test
	void canSaveRetailOffer() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		
		RetailOffer expected_value = new RetailOffer();
		
		expected_value.setId(co_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setType_of_product(type_of_product);
		
		Mockito.when(coRepo.save(expected_value)).thenReturn(expected_value);
		
		RetailOffer coReturned = serviceUnderTest.saveOffer(expected_value);
		
		ArgumentCaptor<RetailOffer> coArgumentCaptor = ArgumentCaptor.forClass(RetailOffer.class);
		
		verify(coRepo).save(coArgumentCaptor.capture());
		
		RetailOffer capturedCivilOffer = coArgumentCaptor.getValue();
		
		assertEquals(capturedCivilOffer.getProducer_id(),expected_value.getProducer_id());
		assertEquals(coReturned.getId(),expected_value.getId());

	}
	
	@Test
	void canGetCurrentUsersRetailOffers() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		
		RetailOffer expected_value = new RetailOffer();
		
		expected_value.setId(co_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setType_of_product(type_of_product);
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value);
		
		User mock_producer = new User();
		mock_producer.setId(producers_id);
		
		Mockito.when(userService.getCurrentSessionUser()).thenReturn(mock_producer);

		Mockito.when(coRepo.getCurrentUserCO(producers_id)).thenReturn(expected_list);
		
		List<RetailOffer> coListReturned = serviceUnderTest.getUserCO();
		
		assertEquals(coListReturned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canDeleteSpecificRetailOffer() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		
		RetailOffer expected_value = new RetailOffer();
		
		expected_value.setId(co_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setType_of_product(type_of_product);
		
		serviceUnderTest.delete(co_id);
		
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		
		verify(coRepo).deleteById(idArgumentCaptor.capture());
		
		Long captured_id = idArgumentCaptor.getValue();
		
		assertEquals(captured_id,co_id);
		

	}
	
	@Test
	void canGetSpecificRetailOffer() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		
		RetailOffer expected_value = new RetailOffer();
		
		expected_value.setId(co_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setType_of_product(type_of_product);
		
		Mockito.when(coRepo.findById(co_id)).thenReturn(Optional.of(expected_value));
		
		RetailOffer returned = serviceUnderTest.findCOffer(co_id);
		
		assertEquals(returned.getId(),expected_value.getId());
	}
	
	@Test
	void canGetAllRetailOffers() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		
		RetailOffer expected_value = new RetailOffer();
		
		expected_value.setId(co_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setType_of_product(type_of_product);
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value);
		
		Mockito.when(coRepo.findAll()).thenReturn(expected_list);
		
		List<RetailOffer> returned_list = serviceUnderTest.getAllOffers();
		
		assertEquals(returned_list.get(0).getId(),expected_list.get(0).getId());
	}
	
	@Test
	void canGetSpecificRetailOfferAndReturnProducersId() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		
		RetailOffer expected_value = new RetailOffer();
		
		expected_value.setId(co_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setType_of_product(type_of_product);
		
		Mockito.when(coRepo.getById(co_id)).thenReturn(expected_value);
		
		Long returned_producers_id = serviceUnderTest.getCOProd_id(co_id);
		
		assertEquals(returned_producers_id,producers_id);
	}
	
	@Test
	void canGetRetailOffersByType() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		Float price_per_kg = 1f;
		
		RetailOffer expected_value1 = new RetailOffer();
		
		expected_value1.setId(co_id);
		expected_value1.setProducer_id(producers_id);
		expected_value1.setType_of_product(type_of_product);
		expected_value1.setPrice_per_kg(price_per_kg);
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value1);
		
		
		Mockito.when(coRepo.getFilteredCO(type_of_product)).thenReturn(expected_list);
		
		List<RetailOffer> returned_list  = serviceUnderTest.getFiltered(type_of_product);
		
		assertEquals(returned_list.get(0).getType_of_product(),expected_list.get(0).getType_of_product());
		
	}
	
	@Test
	void canGetRetailOffersByTypeAndSortingIsAscending() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		Float price_per_kg = 1f;
		
		RetailOffer expected_value1 = new RetailOffer();
		
		expected_value1.setId(co_id);
		expected_value1.setProducer_id(producers_id);
		expected_value1.setType_of_product(type_of_product);
		expected_value1.setPrice_per_kg(price_per_kg);
		
		
		Long co_id2 = 2l;
		String type_of_product2 = "type";
		Float price_per_kg2 = 2f;
		
		RetailOffer expected_value2 = new RetailOffer();
		
		expected_value2.setId(co_id2);
		expected_value2.setProducer_id(producers_id);
		expected_value2.setType_of_product(type_of_product2);
		expected_value2.setPrice_per_kg(price_per_kg2);
		
		
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value1);
		expected_list.add(expected_value2);
		
		
		
		
		Mockito.when(coRepo.getFilteredCOAsc(type_of_product)).thenReturn(expected_list);
		
		List<RetailOffer> returned_list  = serviceUnderTest.getFiltered(type_of_product,"ascending");
		
		verify(coRepo).getFilteredCOAsc(type_of_product);
		assertEquals(returned_list.get(0).getType_of_product(),expected_list.get(0).getType_of_product());
		assertEquals(returned_list.get(1).getType_of_product(),expected_list.get(1).getType_of_product());
		assertEquals(returned_list.get(0).getPrice_per_kg(),expected_list.get(0).getPrice_per_kg());
		assertEquals(returned_list.get(1).getPrice_per_kg(),expected_list.get(1).getPrice_per_kg());


	}
	
	@Test
	void canGetRetailOffersByTypeAndSortingIsDescending() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		Float price_per_kg = 1f;
		
		RetailOffer expected_value1 = new RetailOffer();
		
		expected_value1.setId(co_id);
		expected_value1.setProducer_id(producers_id);
		expected_value1.setType_of_product(type_of_product);
		expected_value1.setPrice_per_kg(price_per_kg);
		
		
		Long co_id2 = 2l;
		String type_of_product2 = "type";
		Float price_per_kg2 = 2f;
		
		RetailOffer expected_value2 = new RetailOffer();
		
		expected_value2.setId(co_id2);
		expected_value2.setProducer_id(producers_id);
		expected_value2.setType_of_product(type_of_product2);
		expected_value2.setPrice_per_kg(price_per_kg2);
		
		
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value2);
		expected_list.add(expected_value1);
		
		
		Mockito.when(coRepo.getFilteredCODesc(type_of_product)).thenReturn(expected_list);
		
		List<RetailOffer> returned_list  = serviceUnderTest.getFiltered(type_of_product,"descending");
		
		verify(coRepo).getFilteredCODesc(type_of_product);
		assertEquals(returned_list.get(0).getType_of_product(),expected_list.get(0).getType_of_product());
		assertEquals(returned_list.get(1).getType_of_product(),expected_list.get(1).getType_of_product());
		assertEquals(returned_list.get(0).getPrice_per_kg(),expected_list.get(0).getPrice_per_kg());
		assertEquals(returned_list.get(1).getPrice_per_kg(),expected_list.get(1).getPrice_per_kg());


	}
	
	@Test
	void canGetRetailOffersByTypeAndSortingIsAnythingElseWillReturnDescending() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		Float price_per_kg = 1f;
		
		RetailOffer expected_value1 = new RetailOffer();
		
		expected_value1.setId(co_id);
		expected_value1.setProducer_id(producers_id);
		expected_value1.setType_of_product(type_of_product);
		expected_value1.setPrice_per_kg(price_per_kg);
		
		
		Long co_id2 = 2l;
		String type_of_product2 = "type";
		Float price_per_kg2 = 2f;
		
		RetailOffer expected_value2 = new RetailOffer();
		
		expected_value2.setId(co_id2);
		expected_value2.setProducer_id(producers_id);
		expected_value2.setType_of_product(type_of_product2);
		expected_value2.setPrice_per_kg(price_per_kg2);
		
		
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value2);
		expected_list.add(expected_value1);
		
		
		Mockito.when(coRepo.getFilteredCODesc(type_of_product)).thenReturn(expected_list);
		
		List<RetailOffer> returned_list  = serviceUnderTest.getFiltered(type_of_product,"anything");
		
		verify(coRepo).getFilteredCODesc(type_of_product);
		assertThat(returned_list.size()).isEqualTo(expected_list.size());
		
	}
	
	@Test
	void canGetRetailOffersByTypeAndSortingIsAscendingAndMaxPrice() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		Float price_per_kg = 1f;
		
		RetailOffer expected_value1 = new RetailOffer();
		
		expected_value1.setId(co_id);
		expected_value1.setProducer_id(producers_id);
		expected_value1.setType_of_product(type_of_product);
		expected_value1.setPrice_per_kg(price_per_kg);
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value1);
		
		Mockito.when(coRepo.getFilteredCOMaxAsc(type_of_product, price_per_kg)).thenReturn(expected_list);
		
		List<RetailOffer> returned_list  = serviceUnderTest.getFiltered(type_of_product,"ascending",price_per_kg);
		
		verify(coRepo).getFilteredCOMaxAsc(type_of_product,price_per_kg);
		assertThat(returned_list.size()).isEqualTo(expected_list.size());
	}
	
	@Test
	void canGetRetailOffersByTypeAndSortingIsAnythingAndMaxPriceWillReturnDescending() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		Float price_per_kg = 1f;
		
		RetailOffer expected_value1 = new RetailOffer();
		
		expected_value1.setId(co_id);
		expected_value1.setProducer_id(producers_id);
		expected_value1.setType_of_product(type_of_product);
		expected_value1.setPrice_per_kg(price_per_kg);
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value1);
		
		Mockito.when(coRepo.getFilteredCOMaxDesc(type_of_product, price_per_kg)).thenReturn(expected_list);
		
		List<RetailOffer> returned_list  = serviceUnderTest.getFiltered(type_of_product,"anything",price_per_kg);
		
		verify(coRepo).getFilteredCOMaxDesc(type_of_product,price_per_kg);
		assertThat(returned_list.size()).isEqualTo(expected_list.size());
	}
	
	@Test
	void canGetRetailOffersByTypeAndMaxPrice() {
		Long producers_id = 1l;
		Long co_id = 1l;
		String type_of_product = "type";
		Float price_per_kg = 1f;
		
		RetailOffer expected_value1 = new RetailOffer();
		
		expected_value1.setId(co_id);
		expected_value1.setProducer_id(producers_id);
		expected_value1.setType_of_product(type_of_product);
		expected_value1.setPrice_per_kg(price_per_kg);
		
		List<RetailOffer> expected_list = new ArrayList<>();
		expected_list.add(expected_value1);
		
		Mockito.when(coRepo.getFilteredCOMax(type_of_product, price_per_kg)).thenReturn(expected_list);
		
		List<RetailOffer> returned_list  = serviceUnderTest.getFiltered(type_of_product,price_per_kg);
		
		verify(coRepo).getFilteredCOMax(type_of_product,price_per_kg);
		assertThat(returned_list.size()).isEqualTo(expected_list.size());
	}
	
}
