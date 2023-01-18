package com.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.Model.CartItem;
import com.Repos.CartItemRepo;

@Service
public class CartItemService {
	
	@Autowired
	private CartItemRepo cartItemRepo;
	
	@Autowired
	private UserService userService;
	
	public void saveCartItem(CartItem item) {
		cartItemRepo.save(item);
	}
	
	public List<CartItem> getUserCI(){
		
		Long currentUserID = userService.getCurrentSessionUser().getId();
		
		List<CartItem> userCIs = cartItemRepo.getCurrentUserCI(currentUserID);
	
		return userCIs;
		
	}
	
	public List<CartItem> getUserCIProd(){
		
		Long currentUserID = userService.getCurrentSessionUser().getId();
		
		List<CartItem> userCIs = cartItemRepo.getCurrentUserCIProd(currentUserID);
	
		return userCIs;
		
	}
	
	public void deleteCartItemByID(Long ID){
	
		cartItemRepo.deleteById(ID);
	}
	
	public CartItem findCartItemByID(Long ID){
		
		CartItem item  =  cartItemRepo.findById(ID).orElse(null);
		return item;
	}

}
