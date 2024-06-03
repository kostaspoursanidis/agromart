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
import org.springframework.test.context.TestPropertySource;

import com.model.Rating;
import com.repos.RatingRepo;


@DataJpaTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
public class RatingRepoTest {

	

	
	@Autowired 
	private RatingRepo ratingRepoTest;
	
	private List<Rating> expected = new ArrayList<>();
	
	private Long rated_id = (long) 1;
	private Long author1 = (long) 2;
	private Long author2 = (long) 3;

	
	@BeforeEach
	void setUp() {
		Rating r1 = new Rating();
		Rating r2 = new Rating();
	
		r1.setRated_user_id(rated_id);
		r2.setRated_user_id(rated_id);
		r1.setAuthor_id(author1);
		r2.setAuthor_id(author2);
		r1.setRating(4f);
		r2.setRating(3f);
		
		
		
		ratingRepoTest.save(r1);
		ratingRepoTest.save(r2);
		
		expected.add(r1);
		expected.add(r2);
		
	}
	
	@AfterEach
    void tearDown() {
		ratingRepoTest.deleteAll();
        expected.clear();
    }
	
	@Test
	void getAllUserRatings() {
		
		List<Rating> querried = ratingRepoTest.getAllUserRatings(rated_id);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getRatedUserId(),querried.get(0).getRatedUserId());
		assertEquals(expected.get(0).getAuthorId(),querried.get(0).getAuthorId());
		assertEquals(expected.get(0).getRating(),querried.get(0).getRating());
		assertEquals(expected.get(1).getRatedUserId(),querried.get(1).getRatedUserId());
		assertEquals(expected.get(1).getAuthorId(),querried.get(1).getAuthorId());
		assertEquals(expected.get(1).getRating(),querried.get(1).getRating());
		

	}
	
	@Test
	void getSpecificUserRating() {
		Rating querried = ratingRepoTest.getSpecificUserRating(rated_id,author1);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getRatedUserId(),querried.getRatedUserId());
		assertEquals(expected.get(0).getAuthorId(),querried.getAuthorId());
		assertEquals(expected.get(0).getRating(),querried.getRating());
	}

}
