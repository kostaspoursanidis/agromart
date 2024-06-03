package com.serviceTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.model.Rating;
import com.repos.RatingRepo;
import com.services.RatingService;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {
	
	@Mock
	private RatingRepo ratingRepo;
	
	@InjectMocks
	private RatingService serviceUnderTest;
	
	@Test
	void canGetAllRatingsForSpecifiedUser() {
		Long users_id = 1l;
		Long authors_id=2l;
		Float rating = 3f;
		Rating return_value = new Rating();//mock(Rating.class);
		
		return_value.setRated_user_id(users_id);
		return_value.setAuthor_id(authors_id);
		return_value.setRating(rating);
		
		List<Rating> repoReturnList = new ArrayList<>();
		
		repoReturnList.add(return_value);
		
		Mockito.when(ratingRepo.getAllUserRatings(users_id)).thenReturn(repoReturnList);
		
		List<Rating> ratings_returned = serviceUnderTest.getAllUserRating(users_id);
		
		assertEquals(repoReturnList.size(),ratings_returned.size());
		assertEquals(repoReturnList.get(0).getRatedUserId(),ratings_returned.get(0).getRatedUserId());
	}
	
	@Test
	void canSaveToRepoViaService() {
		Long users_id = 1l;
		Long authors_id=2l;
		Float rating = 3f;
		Rating return_value = new Rating();//mock(Rating.class);
		
		return_value.setRated_user_id(users_id);
		return_value.setAuthor_id(authors_id);
		return_value.setRating(rating);
		
		serviceUnderTest.save(return_value);
		
		ArgumentCaptor<Rating> ratingArgumentCaptor = ArgumentCaptor.forClass(Rating.class);
		
		verify(ratingRepo).save(ratingArgumentCaptor.capture());
		
		Rating capturedRating = ratingArgumentCaptor.getValue();
		
		assertEquals(capturedRating.getRatedUserId(),return_value.getRatedUserId());
	}
	
	@Test
	void canGetSpecficUserRating() {
		Long users_id = 1l;
		Long authors_id=2l;
		Float rating = 3f;
		Rating return_value = new Rating();//mock(Rating.class);
		
		return_value.setRated_user_id(users_id);
		return_value.setAuthor_id(authors_id);
		return_value.setRating(rating);
		
		
		Mockito.when(ratingRepo.getSpecificUserRating(users_id,authors_id)).thenReturn(return_value);
		
		Rating rating_returned = serviceUnderTest.getSpecificUserRating(users_id,authors_id);
		
		assertEquals(return_value.getRatedUserId(),rating_returned.getRatedUserId());
		assertEquals(return_value.getAuthorId(),rating_returned.getAuthorId());
	}

}
