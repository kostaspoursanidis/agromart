package com.Services;

import java.util.List;

import com.Model.Rating;
import com.Repos.RatingRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private RatingRepo ratingRepo;

    public List<Rating> getAllUserRating(Long id){

        List<Rating> ratings = ratingRepo.getAllUserRatings(id);

        return ratings;

    }

    public void save(Rating newRating) {

        ratingRepo.save(newRating);
    }

    public Rating getSpecificUserRating(Long id,Long auth_id){
        return ratingRepo.getSpecificUserRating(id, auth_id);
    }

    
}
