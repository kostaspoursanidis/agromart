package com.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.Rating;

public interface RatingRepo extends JpaRepository<Rating,Long>{

    @Query("FROM Rating o WHERE o.rated_user_id=:p")  
	public List<Rating> getAllUserRatings(@Param("p") Long id);

    @Query("FROM Rating o WHERE o.rated_user_id=:p AND o.author_id=:a")  
	public Rating getSpecificUserRating(@Param("p") Long id,@Param("a") Long auth_id);
    
}
