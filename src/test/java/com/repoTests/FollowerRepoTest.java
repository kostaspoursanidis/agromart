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

import com.model.Follower;
import com.repos.FollowerRepo;

@DataJpaTest
@TestPropertySource(
        locations = "classpath:test.properties"
)
public class FollowerRepoTest {
	
	
	@Autowired 
	private FollowerRepo followerRepoTest;
	
	private List<Follower> expected = new ArrayList<>();
	
	private Long follower_id = (long) 1;
	private Long followee_id1 = (long) 2;
	private Long followee_id2 = (long) 3;

	
	@BeforeEach
	void setUp() {
		Follower f1 = new Follower();
		Follower f2 = new Follower();
		
		f1.setFollow_user_id(follower_id);
		f1.setFollowee_id(followee_id1);
		f2.setFollow_user_id(follower_id);
		f2.setFollowee_id(followee_id2);
		
		followerRepoTest.save(f1);
		followerRepoTest.save(f2);
		
		expected.add(f1);
		expected.add(f2);
		
	}
	
	@AfterEach
    void tearDown() {
		followerRepoTest.deleteAll();
        expected.clear();
    }
	
	@Test
	void getSpecificFollower() {
		Follower querried = followerRepoTest.getFollower(follower_id, followee_id1);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getFolloweeID(),querried.getFolloweeID());
		assertEquals(expected.get(0).getFollowedUserID(),querried.getFollowedUserID());
	}
	
	@Test
	void getAllFollowers() {
		List<Follower> querried = followerRepoTest.getAllFollowers(follower_id);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.size(),querried.size());
		assertEquals(expected.get(0).getFolloweeID(),querried.get(0).getFolloweeID());
		assertEquals(expected.get(0).getFollowedUserID(),querried.get(0).getFollowedUserID());
		assertEquals(expected.get(1).getFolloweeID(),querried.get(1).getFolloweeID());
		assertEquals(expected.get(1).getFollowedUserID(),querried.get(1).getFollowedUserID());
	}
	
	@Test
	void getAllFollowees() {
		List<Follower> querried = followerRepoTest.getAllFollowees(followee_id1);
		
		assertThat(querried).isNotNull();
		assertEquals(expected.get(0).getFolloweeID(),querried.get(0).getFolloweeID());
		assertEquals(expected.get(0).getFollowedUserID(),querried.get(0).getFollowedUserID());
		
	}

}
