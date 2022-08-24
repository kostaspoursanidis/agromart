package com.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Dto.UserDto;
import com.Model.User;
import com.Repos.UserRepo;

@Service
public class UserServiceImpl implements UserService {
	
	
	
	private UserRepo userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	

	public UserServiceImpl(UserRepo userRepository) {
		super();
		this.userRepository = userRepository;
	}



	@Override
	public User save(UserDto registrationDto) {
		User newUser = new User(registrationDto.getName(), registrationDto.getEmail(),passwordEncoder.encode(registrationDto.getPassword())
				,registrationDto.getRole(),registrationDto.getAddress(),null,registrationDto.getPhoneNum(),null);
		
		return userRepository.save(newUser);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String role){
		
		Collection<SimpleGrantedAuthority> dummy  = new ArrayList<SimpleGrantedAuthority>();
		if(role.equals("buyer")){
			dummy.add(new SimpleGrantedAuthority("ROLE_BUYER"));
		}else if(role.equals("producer")){
			dummy.add(new SimpleGrantedAuthority("ROLE_PRODUCER"));
		}else{
			dummy.add(new SimpleGrantedAuthority("ROLE_CIVILIAN"));
		}
		
		return dummy;
	}
	
	
	public User getCurrentSessionUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails)principal).getUsername();
		User user = userRepository.findByEmail(username);
		
		return user;
	}

	public List<User> getAllUsers(){
		return userRepository.findAll();
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

}
