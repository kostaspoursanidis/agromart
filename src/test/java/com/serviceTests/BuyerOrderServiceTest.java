package com.serviceTests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
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

import com.model.BuyerOrder;
import com.model.User;
import com.repos.BuyerOrderRepo;
import com.services.BuyerOrderService;
import com.services.UserService;

@ExtendWith(MockitoExtension.class)
public class BuyerOrderServiceTest {
	
	@Mock
	private BuyerOrderRepo boRepo;
	
	@Mock
	private UserService userServ;
	
	@InjectMocks
	private BuyerOrderService serviceUnderTest;

	@Test
	void canSaveOrderToRepoViaService() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		
		BuyerOrder return_value = new BuyerOrder();
		
		return_value.setBuyer_id(buyers_id);
		return_value.setProducer_id(producers_id);
		return_value.setApprox_kilos_wanted(approx_kilos);
		
		serviceUnderTest.saveOffer(return_value);
		
		ArgumentCaptor<BuyerOrder> boArgumentCaptor = ArgumentCaptor.forClass(BuyerOrder.class);
		
		verify(boRepo).save(boArgumentCaptor.capture());
		
		BuyerOrder capturedBuyerOffer = boArgumentCaptor.getValue();
		
		assertEquals(capturedBuyerOffer.getProducer_id(),return_value.getProducer_id());
	}
	
	@Test
	void canGetBuyersAllBuyerOrders() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		
		BuyerOrder expected_value = new BuyerOrder();
		
		List<BuyerOrder> expectedList = new ArrayList<>();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setApprox_kilos_wanted(approx_kilos);
		
		expectedList.add(expected_value);
		
		User mocked_buyer = new User();
		mocked_buyer.setId(buyers_id);
		
		
		Mockito.when(userServ.getCurrentSessionUser()).thenReturn(mocked_buyer);
		
		Mockito.when(boRepo.getCurrentUserBO(buyers_id)).thenReturn(expectedList);
		
		List<BuyerOrder> buyerOffers_returned = serviceUnderTest.getUserBO();
		
		assertEquals(buyerOffers_returned.get(0).getBuyer_id(),expectedList.get(0).getBuyer_id());
		
	}
	
	@Test
	void canGetProducersAllBuyerOrders() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		
		BuyerOrder expected_value = new BuyerOrder();
		
		List<BuyerOrder> expectedList = new ArrayList<>();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setApprox_kilos_wanted(approx_kilos);
		
		expectedList.add(expected_value);
		
		User mocked_producer = new User();
		mocked_producer.setId(producers_id);
		
		Mockito.when(userServ.getCurrentSessionUser()).thenReturn(mocked_producer);
		
		Mockito.when(boRepo.getCurrentUserBOProd(producers_id)).thenReturn(expectedList);
		
		List<BuyerOrder> buyerOffers_returned = serviceUnderTest.getUserBOProd();
		
		assertEquals(buyerOffers_returned.get(0).getProducer_id(),expectedList.get(0).getProducer_id());
		
	}
	
	@Test
	void canDeleteBuyerOrderById() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		Long bo_id = 1l;
		
		BuyerOrder expected_value = new BuyerOrder();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setApprox_kilos_wanted(approx_kilos);
		expected_value.setId(bo_id);
		
		
		serviceUnderTest.delete(bo_id);
		
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		
		verify(boRepo).deleteById(idArgumentCaptor.capture());
		
		Long capturedBuyerOfferId = idArgumentCaptor.getValue();
		
		assertEquals(capturedBuyerOfferId,expected_value.getId());
		
	}
	
	@Test
	void canFindBuyerOrderById() {
		
		BuyerOrder expected_value = mock(BuyerOrder.class);
		
		Mockito.when(boRepo.findById(expected_value.getId())).thenReturn(Optional.of(expected_value));
		
		serviceUnderTest.findBOffer(expected_value.getId());
					
		verify(boRepo).findById(expected_value.getId());
		
	}
	
	
	
	@Test
	void canFindAllBuyerOrders() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		Long bo_id = 1l;
		
		BuyerOrder expected_value = new BuyerOrder();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setApprox_kilos_wanted(approx_kilos);
		expected_value.setId(bo_id);
		
		List<BuyerOrder> expected_values = new ArrayList<>();
		expected_values.add(expected_value);
		
		Mockito.when(boRepo.findAll()).thenReturn(expected_values);
		
		List<BuyerOrder> returned_list = serviceUnderTest.getAllOffers();
		
		
		
		assertEquals(returned_list.get(0).getId(),expected_values.get(0).getId());
		
	}
	
	@Test
	void canFindAllBuyerOrdersByTypeOfProduct() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		Long bo_id = 1l;
		String type_of_product = "type";
		
		BuyerOrder expected_value = new BuyerOrder();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setApprox_kilos_wanted(approx_kilos);
		expected_value.setId(bo_id);
		expected_value.setType_of_product(type_of_product);
		
		List<BuyerOrder> expected_values = new ArrayList<>();
		expected_values.add(expected_value);
		
		Mockito.when(boRepo.getFilteredBO(type_of_product)).thenReturn(expected_values);
		
		List<BuyerOrder> returned_list = serviceUnderTest.getFiltered(type_of_product);
		
		
		
		assertEquals(returned_list.get(0).getType_of_product(),expected_values.get(0).getType_of_product());
		
	}
	
	
	
	
	
}
