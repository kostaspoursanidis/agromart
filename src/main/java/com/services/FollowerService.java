package com.services;

import java.util.List;

import com.model.Follower;
import com.repos.FollowerRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepo followerRepo;

    public void deleteFollower(Follower followers){
        
        followerRepo.delete(followers);
    }

    public Follower getFollower(Long follower_id,Long followee_id){
        return followerRepo.getFollower(follower_id, followee_id);
    }

    public List<Follower> getAllFollowers(Long follower_id){
        return followerRepo.getAllFollowers(follower_id);
    }

    public void saveFollower(Follower followers){
        followerRepo.save(followers);
    }
    
    public List<Follower> getAllFollowees(Long followee_id){
    	return followerRepo.getAllFollowees(followee_id);
    }

}
