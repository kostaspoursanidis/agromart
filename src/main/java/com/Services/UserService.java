package com.Services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.Dto.UserDto;
import com.Model.User;

public interface UserService extends UserDetailsService {
	User save(UserDto registrationDto);

}
