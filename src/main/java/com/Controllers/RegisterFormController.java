package com.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Dto.UserDto;
import com.Services.UserService;
import com.Services.UserServiceImpl;

@Controller
@RequestMapping("/register")
public class RegisterFormController {
	
	private UserService userService;

	@Autowired
	private UserServiceImpl userServImpl;
	
	
	public RegisterFormController(UserService userService) {
		super();
		this.userService = userService;
	}




	@GetMapping
	public String showRegister(Model model) {
		model.addAttribute("user", new UserDto());
		return "register";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserDto registrationDto) {

		if(userServImpl.checkIfExists(registrationDto.getEmail())){
			userService.save(registrationDto);
			return "redirect:/register?success";
		}else{
			return "redirect:/register?failure";
		}
	}

}
