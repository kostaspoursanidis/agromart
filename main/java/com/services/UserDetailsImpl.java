package com.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

//import com.model.User;
import com.repos.UserRepo;

@Service
public class UserDetailsImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		com.model.User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		return buildUser(user); // 5/2
		//return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));		
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
	
	// 5/2
	private User buildUser(com.model.User user) {
		
		String username               = user.getId().toString();
        String password               = user.getPassword();
        boolean enabled               = true;
        boolean accountNonExpired     = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked      = true;
        Collection<? extends GrantedAuthority> authorities = mapRolesToAuthorities(user.getRole());
        

        User springUser = new User(username, 
                                   password, 
                                   enabled,
                                   accountNonExpired, 
                                   credentialsNonExpired, 
                                   accountNonLocked,
                                   authorities);
        return springUser;
	}
}
