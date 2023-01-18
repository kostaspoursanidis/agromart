package com.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.Model.User;
import com.Repos.UserRepo;

@Service
public class UserService  { 
	
	
	@Autowired 
	private UserRepo userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private FruitService fruitService;
	
	
	public User save(User newUser) {
		String password = newUser.getPassword();
		String encoded = passwordEncoder.encode(password);
		newUser.setPassword(encoded);
		
		return userRepository.save(newUser);
	}
	
	
	public User getCurrentSessionUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails)principal).getUsername();
		User user = userRepository.findByEmail(username);
		
		return user;
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	public List<User> getAllBuyers(){
		return userRepository.getUsersBuyers();
	}
	
	public List<User> getAllProducers(){
		return userRepository.getUsersProd();
	}

	public User getById(Long id){
		return userRepository.findById(id).orElse(null);
	}

	public boolean checkIfExists(String email){
		
		User user = userRepository.findByEmail(email);
		if(user==null){
			return true;
		}else{
			return false;
		}
	}

	public void editUser(User user,Boolean passChanged ){
		
		if(passChanged==true){
			String encoded = passwordEncoder.encode(user.getPassword()); 
			user.setPassword(encoded);
		}									
		userRepository.save(user);
	}
	
	//add filtering methods here 
	
	public List<User> getFilteredByProduceAndRating(String produce,Float rating){
		List<User> finaluser = new ArrayList<User>();
		
		List<User> beforeProduce = userRepository.getUsersByRating(rating);
		
		if(!produce.equals("any")) {	
			
			for (User user : beforeProduce) {
				if(fruitService.hasFruit(user.getId(), produce)) {
					finaluser.add(user);
				}else{
					continue;
				}
			}
		}else {
			
			return beforeProduce;
		}
		
		return finaluser;
	}
	
	public List<User> getFilteredByProduceAndRatingAndName(String produce,Float rating,String name){
		List<User> finaluser = new ArrayList<User>();
		
		List<User> beforeProduce = userRepository.getUsersByRatingAndName(rating,name);
		
		if(!produce.equals("any")) {
			for (User user : beforeProduce) {
				if(fruitService.hasFruit(user.getId(), produce)) {
					finaluser.add(user);
				}else{
					continue;
				}
			}
		}else {
			return beforeProduce;
		}
		
		return finaluser;
	}
	
	public List<User> getFilteredByProduce(String produce){
		List<User> finaluser = new ArrayList<User>();
		
		List<User> before = userRepository.getUsersProd();
		
		if(!produce.equals("any")) {
			for (User user : before) {
				if(fruitService.hasFruit(user.getId(), produce)) {
					finaluser.add(user);
				}else{
					continue;
				}
			}
		}else {
			return before;
		}
		
		return finaluser;
	}
	
	public List<User> getFilteredByProduceAndName(String produce,String name){
		List<User> finaluser = new ArrayList<User>();
		
		List<User> before = userRepository.getUsersProdwithName(name);
		
		if(!produce.equals("any")) {
			for (User user : before) {
				if(fruitService.hasFruit(user.getId(), produce)) {
					finaluser.add(user);
				}else{
					continue;
				}
			}
		}else {
			return before;
		}
		
		return finaluser;
	}
	
	public List<User> getBuyersFilteredByName(String name){
		
		List<User> finaluser = userRepository.getUserswithName(name);
	
		return finaluser;
	}
	
	public List<User> getBuyersFilteredByRatingAndName(Float rating,String name){
		
		List<User> finaluser = userRepository.getUsersByRatingAndName(rating,name);
		
		return finaluser;
	}
	
	public List<User> getBuyersFilteredByRating(Float rating){
		
		List<User> finaluser = userRepository.getUsersByRating(rating);
		
		return finaluser;
	}

}
