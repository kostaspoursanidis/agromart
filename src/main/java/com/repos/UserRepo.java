package com.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.User;

public interface UserRepo extends JpaRepository<User, Long>{
	User findByEmail(String email);
	
	@Query("FROM User o WHERE rating >= :r")
	public List<User> getUsersByRating(@Param("r") Float rating);
	
	@Query("FROM User o WHERE o.rating >= :r AND o.name LIKE %:n% ")
	public List<User> getUsersByRatingAndName(@Param("r") Float rating, @Param("n") String name);
	
	@Query("FROM User o WHERE o.role = 'producer'")
	public List<User> getUsersProd();
	
	@Query("FROM User o WHERE o.role = 'buyer'")
	public List<User> getUsersBuyers();
	
	@Query("FROM User o WHERE o.role = 'producer' AND o.name LIKE %:n% ")
	public List<User> getUsersProdwithName(@Param("n")String name);

	@Query("FROM User o WHERE o.name LIKE %:n% ")
	public List<User> getUserswithName(@Param("n")String name);











}



