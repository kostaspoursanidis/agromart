package com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model.User;
import com.services.EmailService;
import com.services.UserService;

@Controller
public class RegisterAndLoginController {
	
	@Autowired
	private UserService userServImpl;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("user", new User()); //was dto became user when refactoring
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUserAccount(@ModelAttribute("user") User registrationDto) {

		if(userServImpl.checkIfExists(registrationDto.getEmail())){
			userServImpl.save(registrationDto);
			return "redirect:/register?success";
		}else{
			return "redirect:/register?failure";
		}
	}
	
	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}
	
	// FORGOT PASSWORD METHODS
	@GetMapping("/forgotpassword")
	public String getForgotPassForm() {
		return "forgotPassForm";
	}
	
	@PostMapping("/forgotpassword")
	public String postForgotPassForm(@RequestParam("email") String email) {
		User f_user = userServImpl.findByEmail(email);
		if(f_user == null) {
			return "redirect:/login?notFound";
		}else {
			String id = Long.toString(f_user.getId());
			emailService.sendMail(email, "Reset Your Password",
					"You have requested a password reset. Please follow the link to reset your password: localhost:8080/resetpassword/"+id);
		}
		return "redirect:/login?found";
	}
	
	@GetMapping("/resetpassword/{id}")
	public String getResetPassForm(@PathVariable("id") Long id,Model model) {
		User temp_user = new User();
		temp_user.setId(id);	
		model.addAttribute("id",temp_user);
		return "resetPassForm";
	}
	
	@PostMapping("/resetpassword")
	public String postResetPassForm(@RequestParam("pass") String newPass,@ModelAttribute("id") User id) {
		User r_user = userServImpl.getById(id.getId());
		r_user.setPassword(newPass);
		userServImpl.editUser(r_user, true);
		
		return "redirect:/login";
	}
	
}
