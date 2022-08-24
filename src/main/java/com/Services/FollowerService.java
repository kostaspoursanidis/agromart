package com.Services;

import java.util.List;

import com.Model.Followers;
import com.Repos.FollowerRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepo followerRepo;

    public void deleteFollower(Followers followers){
        
        followerRepo.delete(followers);
    }

    public Followers getFollower(Long follower_id,Long followee_id){
        return followerRepo.getFollower(follower_id, followee_id);
    }

    public List<Followers> getAllFollowers(Long follower_id){
        return followerRepo.getAllFollowers(follower_id);
    }

    public void saveFollower(Followers followers){
        followerRepo.save(followers);
    }

}
