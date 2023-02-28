package com.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.Follower;

public interface FollowerRepo extends JpaRepository<Follower,Long> {

    @Query("FROM Follower f WHERE f.follow_user_id=:p AND f.followee_id=:o")
    public Follower getFollower(@Param("p")Long follower_id,@Param("o") Long followee_id);

    @Query("FROM Follower f WHERE f.follow_user_id=:p ")
    public List<Follower> getAllFollowers(@Param("p")Long follower_id);
    
    @Query("FROM Follower f WHERE f.followee_id=:p ")
    public List<Follower> getAllFollowees(@Param("p")Long followee_id);


    
}
