package com.serviceTests;

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

import com.model.CartItem;
import com.model.User;
import com.repos.CartItemRepo;
import com.services.CartItemService;
import com.services.UserService;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {
	
	@Mock
	private CartItemRepo cartItemRepo;
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private CartItemService serviceUnderTest;
	
	@Test
	void canSaveCartItemToRepoViaService() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		
		CartItem expected_value = new CartItem();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setKilos_wanted(approx_kilos);
		
		serviceUnderTest.saveCartItem(expected_value);
		
		ArgumentCaptor<CartItem> cartItemArgumentCaptor = ArgumentCaptor.forClass(CartItem.class);
		
		verify(cartItemRepo).save(cartItemArgumentCaptor.capture());
		
		CartItem capturedCartItem = cartItemArgumentCaptor.getValue();
		
		assertEquals(capturedCartItem.getProducer_id(),expected_value.getProducer_id());
	}
	
	@Test
	void canGetAllUsersCartItemsWithStatusPending() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		String status = "pending";
		
		CartItem expected_value = new CartItem();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setKilos_wanted(approx_kilos);
		expected_value.setStatus(status);
		
		List<CartItem> expected_list = new ArrayList<>();
		expected_list.add(expected_value);
		
		User mock_buyer = new User();
		mock_buyer.setId(buyers_id);
		
		Mockito.when(userService.getCurrentSessionUser()).thenReturn(mock_buyer);
		Mockito.when(cartItemRepo.getCurrentUserCI(buyers_id)).thenReturn(expected_list);
		
				
		List<CartItem> returned_list = serviceUnderTest.getUserCI();
		
		assertEquals(returned_list.get(0).getBuyer_id(),expected_list.get(0).getBuyer_id());
	}
	
	@Test
	void canGetAllProducersClaimedRetailOrders() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		String status = "sent";
		
		CartItem expected_value = new CartItem();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setKilos_wanted(approx_kilos);
		expected_value.setStatus(status);
		
		List<CartItem> expected_list = new ArrayList<>();
		expected_list.add(expected_value);
		
		User mock_producer = new User();
		mock_producer.setId(producers_id);
		
		Mockito.when(userService.getCurrentSessionUser()).thenReturn(mock_producer);
		Mockito.when(cartItemRepo.getCurrentUserCIProd(producers_id)).thenReturn(expected_list);
		
				
		List<CartItem> returned_list = serviceUnderTest.getUserCIProd();
		
		assertEquals(returned_list.get(0).getBuyer_id(),expected_list.get(0).getBuyer_id());
		assertEquals(returned_list.get(0).getProducer_id(),expected_list.get(0).getProducer_id());
		assertEquals(returned_list.get(0).getStatus(),expected_list.get(0).getStatus());
	}
	
	@Test
	void canDeleteCartItemById() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		Long item_id = 1l;
		
		CartItem expected_value = new CartItem();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setKilos_wanted(approx_kilos);
		expected_value.setId(item_id);
		
		serviceUnderTest.deleteCartItemByID(item_id);
		
		ArgumentCaptor<Long> idItemArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		
		verify(cartItemRepo).deleteById(idItemArgumentCaptor.capture());
		
		Long capturedItemId = idItemArgumentCaptor.getValue();
		
		assertEquals(capturedItemId,expected_value.getId());
	}
	
	@Test
	void canFindCartItemById() {
		Long producers_id = 1l;
		Long buyers_id = 2l;
		Float approx_kilos = 4f;
		String status = "pending";
		Long item_id = 1l;
		
		CartItem expected_value = new CartItem();
		
		expected_value.setBuyer_id(buyers_id);
		expected_value.setProducer_id(producers_id);
		expected_value.setKilos_wanted(approx_kilos);
		expected_value.setStatus(status);
		expected_value.setId(item_id);
		
						
		Mockito.when(cartItemRepo.findById(item_id)).thenReturn(Optional.of(expected_value));
		
				
		CartItem returned_item = serviceUnderTest.findCartItemByID(item_id);
		
		assertEquals(returned_item.getBuyer_id(),expected_value.getBuyer_id());
		assertEquals(returned_item.getId(),expected_value.getId());
		
	}
	
	
	
	
	
	
	
	
	
}
