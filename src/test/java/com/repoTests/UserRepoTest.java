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

import com.Model.User;
import com.Repos.UserRepo;

@DataJpaTest
public class UserRepoTest {

	
	@Autowired
	private UserRepo userRepoTest;
	
	private List<User> expected = new ArrayList<>();
	
	@BeforeEach
	void setUp() {
		
		User producer = new User();
		User civilian = new User();
		User buyer = new User();
		
		producer.setEmail("producer@example.com");
		producer.setName("producer");
		producer.setRating(3f);
		producer.setRole("producer");
		
		civilian.setEmail("civilian@example.com");
		civilian.setName("civilian");
		civilian.setRating(2f);
		civilian.setRole("civilian");
		
		buyer.setEmail("buyer@example.com");
		buyer.setName("buyer");
		buyer.setRating(2f);
		buyer.setRole("buyer");
		
		userRepoTest.save(producer);
		userRepoTest.save(civilian);
		userRepoTest.save(buyer);
		
		expected.add(producer);
		expected.add(civilian);
		expected.add(buyer);
		
		
		
		
	}
	
	@AfterEach
	void tearDown() {
		expected.clear();
		userRepoTest.deleteAll();
	}
	
	@Test
	void canFindByEmail() {
		User querried = userRepoTest.findByEmail("producer@example.com");
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getEmail(),querried.getEmail());
	}
	
	@Test
	void canFindUsersWithRatingGreaterThan() {
		List<User> querried = userRepoTest.getUsersByRating(2.5f);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getEmail(),querried.get(0).getEmail());
		assertEquals(expected.get(0).getRating(),querried.get(0).getRating());
	}
	
	@Test
	void canFindUsersWithRatingGreaterThanAndNameLike() {
		List<User> querried = userRepoTest.getUsersByRatingAndName(2.5f,"producer");
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getEmail(),querried.get(0).getEmail());
		assertEquals(expected.get(0).getRating(),querried.get(0).getRating());
		assertEquals(expected.get(0).getName(),querried.get(0).getName());
		
	}
	
	@Test
	void canFindUsersWithRoleAsProducer() {
		List<User> querried = userRepoTest.getUsersProd();
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getEmail(),querried.get(0).getEmail());
		assertEquals(expected.get(0).getRating(),querried.get(0).getRating());
		assertEquals(expected.get(0).getName(),querried.get(0).getName());
		assertEquals(expected.get(0).getRole(),querried.get(0).getRole());
		
		
	}
	
	@Test
	void canFindUsersWithRoleAsBuyer() {
		List<User> querried = userRepoTest.getUsersBuyers();
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(2).getEmail(),querried.get(0).getEmail());
		assertEquals(expected.get(2).getRating(),querried.get(0).getRating());
		assertEquals(expected.get(2).getName(),querried.get(0).getName());
		assertEquals(expected.get(2).getRole(),querried.get(0).getRole());
		
		
	}
	
	@Test
	void canFindUsersWithRoleAsProducerAndNameLike() {
		List<User> querried = userRepoTest.getUsersProdwithName("producer");
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getEmail(),querried.get(0).getEmail());
		assertEquals(expected.get(0).getRating(),querried.get(0).getRating());
		assertEquals(expected.get(0).getName(),querried.get(0).getName());
		assertEquals(expected.get(0).getRole(),querried.get(0).getRole());
		
		
	}
	
	@Test
	void canFindUsersWithNameLike() {
		List<User> querried = userRepoTest.getUserswithName("civilian");
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(1).getEmail(),querried.get(0).getEmail());
		assertEquals(expected.get(1).getRating(),querried.get(0).getRating());
		assertEquals(expected.get(1).getName(),querried.get(0).getName());
		assertEquals(expected.get(1).getRole(),querried.get(0).getRole());
		
		
	}
	
	
	
	
}
