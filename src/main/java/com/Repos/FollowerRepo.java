package com.Repos;

import java.util.List;

import com.Model.Followers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowerRepo extends JpaRepository<Followers,Long> {

    @Query("FROM Followers f WHERE f.follow_user_id=:p AND f.followee_id=:o")
    public Followers getFollower(@Param("p")Long follower_id,@Param("o") Long followee_id);

    @Query("FROM Followers f WHERE f.follow_user_id=:p ")
    public List<Followers> getAllFollowers(@Param("p")Long follower_id);
    
    @Query("FROM Followers f WHERE f.followee_id=:p ")
    public List<Followers> getAllFollowees(@Param("p")Long followee_id);


    
}
