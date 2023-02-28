package com.serviceTests;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.model.User;
import com.repos.UserRepo;
import com.services.FruitService;
import com.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepo userRepo;
	
	@Mock
	private FruitService fruitService;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserService serviceUnderTest;

	private User getMockUserByRole(String given_role) {
		User user = new User();
		user.setId(1l);
		user.setName("name");
		user.setEmail("email@email.com");
		user.setPassword("password");
		user.setRole(given_role);
		user.setAddress("address");
		user.setPhoneNum("123456789");
		user.setPhoto(null);
							
		return user;
	}
	
	@Test 
	void canCreateNewUser() {
		
		User expected_user = getMockUserByRole("producer");
		
		Mockito.when(userRepo.save(any(User.class))).thenReturn(expected_user);
		Mockito.when(passwordEncoder.encode(any())).thenReturn(expected_user.getPassword());
		
		User returned_user = serviceUnderTest.save(expected_user);
		
		verify(userRepo).save(any(User.class));
		verify(passwordEncoder).encode(any());
		assertEquals(returned_user.getEmail(),expected_user.getEmail());
	}
	
	@Test 
	void canGetUserWithEmail() {
		
		User expected_user = getMockUserByRole("producer");
		
		Mockito.when(userRepo.findByEmail(any(String.class))).thenReturn(expected_user);
		
		User returned_user = serviceUnderTest.findByEmail("email@email.com");
		
		
		assertEquals(returned_user.getEmail(),expected_user.getEmail());
	}
	
	
	@Test
	void canGetAllUsers() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		Mockito.when(userRepo.findAll()).thenReturn(expected_list);
		
		List<User> returned = serviceUnderTest.getAllUsers();
		
		verify(userRepo).findAll();
		assertEquals(returned.get(0).getEmail(),expected_list.get(0).getEmail());
		
	}
	
	@Test
	void canGetAllUsersWithRoleBuyer() {
		User expected_user = getMockUserByRole("buyer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		Mockito.when(userRepo.getUsersBuyers()).thenReturn(expected_list);
		
		List<User> returned = serviceUnderTest.getAllBuyers();
		
		verify(userRepo).getUsersBuyers();
		assertEquals(returned.get(0).getRole(),expected_list.get(0).getRole());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducer() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		Mockito.when(userRepo.getUsersProd()).thenReturn(expected_list);
		
		List<User> returned = serviceUnderTest.getAllProducers();
		
		verify(userRepo).getUsersProd();
		assertEquals(returned.get(0).getRole(),expected_list.get(0).getRole());
		
	}
	
	@Test
	void canGetSpecificUserWithID() {
		User expected_user = getMockUserByRole("producer");
		
		Mockito.when(userRepo.findById(expected_user.getId())).thenReturn(Optional.of(expected_user));
		
		User returned = serviceUnderTest.getById(expected_user.getId());
		
		verify(userRepo).findById(expected_user.getId());
		assertEquals(returned.getId(),expected_user.getId());
		
	}
	
	@Test
	void canCheckIfUserExistsUsingEmail() {
		User expected_user = getMockUserByRole("producer");
		
		Mockito.when(userRepo.findByEmail(expected_user.getEmail())).thenReturn(expected_user);
		
		Boolean returned = serviceUnderTest.checkIfExists(expected_user.getEmail());
		
		verify(userRepo).findByEmail(expected_user.getEmail());
		assertEquals(returned,false);
	}
	
	@Test
	void canCheckIfUserExistsUsingEmailWillReturnTrueIfUserNotExists() {
		Mockito.when(userRepo.findByEmail(any())).thenReturn(null);
		
		Boolean returned = serviceUnderTest.checkIfExists("notExists@example.com");
		
		verify(userRepo).findByEmail("notExists@example.com");
		assertEquals(returned,true);
	}
	
	@Test
	void canEditUserThatPasswordHasNotChanged() {
		User expected_user = getMockUserByRole("producer");
		
		Mockito.when(userRepo.save(expected_user)).thenReturn(expected_user);
		
		serviceUnderTest.editUser(expected_user, false);
		
		verify(userRepo).save(expected_user);
		
	}
	
	@Test
	void canEditUserThatPasswordHasChanged() {
		User expected_user = getMockUserByRole("producer");
		
		Mockito.when(userRepo.save(expected_user)).thenReturn(expected_user);
		Mockito.when(passwordEncoder.encode(expected_user.getPassword())).thenReturn(expected_user.getPassword());
		
		serviceUnderTest.editUser(expected_user, true);
		
		verify(userRepo).save(expected_user);
		verify(passwordEncoder).encode(expected_user.getPassword());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducerAndByProduceType() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		String type_of_produce = "type";
		
		Mockito.when(userRepo.getUsersProd()).thenReturn(expected_list);
		Mockito.when(fruitService.hasFruit(expected_user.getId(), type_of_produce)).thenReturn(true);
		
		List<User> returned = serviceUnderTest.getFilteredByProduce(type_of_produce);
		
		verify(userRepo).getUsersProd();
		verify(fruitService).hasFruit(expected_user.getId(), type_of_produce);
		assertEquals(returned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducerAndByAnyProduceType() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		String type_of_produce = "any";
		
		Mockito.when(userRepo.getUsersProd()).thenReturn(expected_list);

		List<User> returned = serviceUnderTest.getFilteredByProduce(type_of_produce);
		
		verify(userRepo).getUsersProd();
		verify(fruitService, never()).hasFruit(expected_user.getId(), type_of_produce);
		assertEquals(returned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducerAndByProduceTypeAndRatingAboveThreshold() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		String type_of_produce = "type";
		Float rating = 1f;
		
		Mockito.when(userRepo.getUsersByRating(rating)).thenReturn(expected_list);
		Mockito.when(fruitService.hasFruit(expected_user.getId(), type_of_produce)).thenReturn(true);
		
		List<User> returned = serviceUnderTest.getFilteredByProduceAndRating(type_of_produce,rating);
		
		verify(userRepo).getUsersByRating(rating);
		verify(fruitService).hasFruit(expected_user.getId(), type_of_produce);
		assertEquals(returned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducerAndByAnyProduceTypeAndRatingAboveThreshold() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		String type_of_produce = "any";
		Float rating = 1f;
		
		Mockito.when(userRepo.getUsersByRating(rating)).thenReturn(expected_list);

		List<User> returned = serviceUnderTest.getFilteredByProduceAndRating(type_of_produce,rating);
		
		verify(userRepo).getUsersByRating(rating);
		verify(fruitService,never()).hasFruit(expected_user.getId(), type_of_produce);
		assertEquals(returned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducerAndByProduceTypeAndNameLike() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		String type_of_produce = "type";

		Mockito.when(userRepo.getUsersProdwithName(expected_user.getName())).thenReturn(expected_list);
		Mockito.when(fruitService.hasFruit(expected_user.getId(), type_of_produce)).thenReturn(true);
		
		List<User> returned = serviceUnderTest.getFilteredByProduceAndName(type_of_produce,expected_user.getName());
		
		verify(userRepo).getUsersProdwithName(expected_user.getName());
		verify(fruitService).hasFruit(expected_user.getId(), type_of_produce);
		assertEquals(returned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducerAndByAnyProduceTypeAndNameLike() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		String type_of_produce = "any";

		Mockito.when(userRepo.getUsersProdwithName(expected_user.getName())).thenReturn(expected_list);
		
		List<User> returned = serviceUnderTest.getFilteredByProduceAndName(type_of_produce,expected_user.getName());
		
		verify(userRepo).getUsersProdwithName(expected_user.getName());
		verify(fruitService, never()).hasFruit(expected_user.getId(), type_of_produce);
		assertEquals(returned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducerAndByProduceTypeAndNameLikeAndRating() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		String type_of_produce = "type";
		Float rating = 1f;

		Mockito.when(userRepo.getUsersByRatingAndName(rating,expected_user.getName())).thenReturn(expected_list);
		Mockito.when(fruitService.hasFruit(expected_user.getId(), type_of_produce)).thenReturn(true);
		
		List<User> returned = serviceUnderTest.getFilteredByProduceAndRatingAndName(type_of_produce,rating,expected_user.getName());
		
		verify(userRepo).getUsersByRatingAndName(rating,expected_user.getName());
		verify(fruitService).hasFruit(expected_user.getId(), type_of_produce);
		assertEquals(returned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canGetAllUsersWithRoleProducerAndByAnyProduceTypeAndNameLikeAndRating() {
		User expected_user = getMockUserByRole("producer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		String type_of_produce = "any";
		Float rating = 1f;

		Mockito.when(userRepo.getUsersByRatingAndName(rating,expected_user.getName())).thenReturn(expected_list);
		
		List<User> returned = serviceUnderTest.getFilteredByProduceAndRatingAndName(type_of_produce,rating,expected_user.getName());
		
		verify(userRepo).getUsersByRatingAndName(rating,expected_user.getName());
		verify(fruitService,never()).hasFruit(expected_user.getId(), type_of_produce);
		assertEquals(returned.get(0).getId(),expected_list.get(0).getId());
		
	}
	
	@Test
	void canGetAllUserWithRoleBuyerAndNameLike() {
		User expected_user = getMockUserByRole("buyer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		Mockito.when(userRepo.getUserswithName(expected_user.getName())).thenReturn(expected_list);
		
		List<User> returned = serviceUnderTest.getBuyersFilteredByName(expected_user.getName());
		
		verify(userRepo).getUserswithName(expected_user.getName());
		assertEquals(returned.get(0).getName(),expected_list.get(0).getName());
	}
	
	@Test
	void canGetAllUserWithRoleBuyerAndRatingAboveThreshold() {
		User expected_user = getMockUserByRole("buyer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		Float rating = 1f;
		
		Mockito.when(userRepo.getUsersByRating(rating)).thenReturn(expected_list);
		
		List<User> returned = serviceUnderTest.getBuyersFilteredByRating(rating);
		
		verify(userRepo).getUsersByRating(rating);
		assertEquals(returned.get(0).getName(),expected_list.get(0).getName());
	}
	
	@Test
	void canGetAllUserWithRoleBuyerAndRatingAboveThresholdAndNameLike() {
		User expected_user = getMockUserByRole("buyer");
		List<User> expected_list = new ArrayList<>();
		expected_list.add(expected_user);
		
		Float rating = 1f;
		
		Mockito.when(userRepo.getUsersByRatingAndName(rating,expected_user.getName())).thenReturn(expected_list);
		
		List<User> returned = serviceUnderTest.getBuyersFilteredByRatingAndName(rating,expected_user.getName());
		
		verify(userRepo).getUsersByRatingAndName(rating,expected_user.getName());
		assertEquals(returned.get(0).getName(),expected_list.get(0).getName());
	}
	
}
