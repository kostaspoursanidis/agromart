package com.Repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Model.User;

public interface UserRepo extends JpaRepository<User, Long>{
	User findByEmail(String email);
}


