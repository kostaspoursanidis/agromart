package com.serviceTests;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.model.Follower;
import com.repos.FollowerRepo;
import com.services.FollowerService;

@ExtendWith(MockitoExtension.class)
public class FollowerServiceTest {
	
	@Mock
	private FollowerRepo followerRepo;
	
	@InjectMocks
	private FollowerService serviceUnderTest;
	
	@Test
	void canDeleteFollower() {
		Long followed_user_id = 1l;
		Long followee_id = 2l;
		
		Follower expected = new Follower();
		
		expected.setFollow_user_id(followed_user_id);
		expected.setFollowee_id(followee_id);
		
		serviceUnderTest.deleteFollower(expected);
		
		ArgumentCaptor<Follower> followerArgumentCaptor = ArgumentCaptor.forClass(Follower.class);
		verify(followerRepo).delete(followerArgumentCaptor.capture());
		Follower capturedFollower = followerArgumentCaptor.getValue();
		
		assertEquals(capturedFollower.getFollowedUserID(),expected.getFollowedUserID());
		
		
	}
	
	@Test
	void canGetSpecificFollower() {
		Long followed_user_id = 1l;
		Long followee_id = 2l;
		
		Follower expected = new Follower();
		
		expected.setFollow_user_id(followed_user_id);
		expected.setFollowee_id(followee_id);
		
		Mockito.when(followerRepo.getFollower(followed_user_id, followee_id)).thenReturn(expected);
		
		Follower returned  = serviceUnderTest.getFollower(followed_user_id,followee_id);
		
		assertEquals(returned.getFollowedUserID(),expected.getFollowedUserID());
		
		
	}
	
	@Test
	void canGetAllUserFollowers() {
		Long followed_user_id = 1l;
		Long followee_id = 2l;
		
		Follower expected = new Follower();
		
		expected.setFollow_user_id(followed_user_id);
		expected.setFollowee_id(followee_id);
		
		List<Follower> expected_list = new ArrayList<>();
		expected_list.add(expected);
		
		Mockito.when(followerRepo.getAllFollowers(followed_user_id)).thenReturn(expected_list);
		
		List<Follower> returned  = serviceUnderTest.getAllFollowers(followed_user_id);
		
		assertEquals(returned.get(0).getFollowedUserID(),expected_list.get(0).getFollowedUserID());
		assertEquals(returned.get(0).getFolloweeID(),expected_list.get(0).getFolloweeID());
		
		
	}
	
	@Test
	void canSaveFollowerViaService() {
		Long followed_user_id = 1l;
		Long followee_id = 2l;
		
		Follower expected = new Follower();
		
		expected.setFollow_user_id(followed_user_id);
		expected.setFollowee_id(followee_id);
		
		serviceUnderTest.saveFollower(expected);
		
		ArgumentCaptor<Follower> followerArgumentCaptor = ArgumentCaptor.forClass(Follower.class);
		verify(followerRepo).save(followerArgumentCaptor.capture());
		Follower capturedFollower = followerArgumentCaptor.getValue();
		
		assertEquals(capturedFollower.getFollowedUserID(),expected.getFollowedUserID());
		
		
	}
	
	@Test
	void canGetAllUsersThatAUserFollows() {
		Long followed_user_id = 1l;
		Long followee_id = 2l;
		
		Follower expected = new Follower();
		
		expected.setFollow_user_id(followed_user_id);
		expected.setFollowee_id(followee_id);
		
		List<Follower> expected_list = new ArrayList<>();
		expected_list.add(expected);
		
		Mockito.when(followerRepo.getAllFollowees(followee_id)).thenReturn(expected_list);
		
		List<Follower> returned  = serviceUnderTest.getAllFollowees(followee_id);
		
		assertEquals(returned.get(0).getFollowedUserID(),expected_list.get(0).getFollowedUserID());
		assertEquals(returned.get(0).getFolloweeID(),expected_list.get(0).getFolloweeID());
		
		
	}
	
	
	
	

}
