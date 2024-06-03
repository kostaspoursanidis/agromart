package com.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.model.CartItem;
import com.model.RetailOffer;
import com.model.User;
import com.services.CartItemService;
import com.services.EmailService;
import com.services.RetailOfferService;
import com.services.UserService;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private RetailOfferService civilOfferService;

	
	
	
	
	
	
	@GetMapping
    public String showManageCartPage(Model model){

        User user = userService.getCurrentSessionUser();
        model.addAttribute("user", user);

        List<CartItem> userCIs_before = cartItemService.getUserCI();
        List<RetailOffer> cOffersInCart = new ArrayList<RetailOffer>();
        List<CartItem> userCIs = new ArrayList<>(); // 15/1
        for(CartItem item : userCIs_before) { // 15/1
        	if(item.getStatus().equals("pending")) {
        		userCIs.add(item); // 15/1
        		RetailOffer civil_offer = civilOfferService.findCOffer(item.getCivil_offer_id());
        	  	cOffersInCart.add(civil_offer);
        	}
        }
          
        Float totalPrice = 0f;
        for (int i = 0; i < userCIs.size(); i++) {
			totalPrice = totalPrice + userCIs.get(i).getKilos_wanted() * cOffersInCart.get(i).getPrice_per_kg();
			
        }
          
        DecimalFormat df  = new DecimalFormat("0.00");
        model.addAttribute("totalPrice", df.format(totalPrice));
		model.addAttribute("userCIs", userCIs);
		model.addAttribute("cOffers", cOffersInCart);
         

        return "cart";
    }


    @GetMapping("/delete/{id}")
	public String deleteItem(@PathVariable("id") Long id){
		cartItemService.deleteCartItemByID(id);
		return "redirect:/cart";

	}

    @GetMapping("/edit/{id}")
	public String editOffer(@PathVariable("id") Long id,Model model){
		CartItem oldCartItem = cartItemService.findCartItemByID(id); 
		model.addAttribute("item", oldCartItem);

		return "addToCart";
	}

    @GetMapping("/create/{id}")
    public String createCartItem(@PathVariable("id") Long civil_offer_id ,Model model){
        CartItem item = new CartItem();
        item.setCivil_offer_id(civil_offer_id);
        Long prod_id = civilOfferService.getCOProd_id(civil_offer_id);
        item.setProducer_id(prod_id);
        model.addAttribute("item", item);
        
        return "addToCart";
    }

    @PostMapping("/create/save")
    public String saveCartItem(@ModelAttribute("item") CartItem item){

        User user = userService.getCurrentSessionUser();
        item.setBuyer_id(user.getId());
        item.setStatus("pending");
        cartItemService.saveCartItem(item);
        return "redirect:/homepage";
    }
    
    @GetMapping("/checkout")
    public String checkoutDone() {
    	User user = userService.getCurrentSessionUser();
    	List<CartItem> userCIs = cartItemService.getUserCI();
    	for (CartItem item : userCIs) {
    		if(item.getStatus().equals("pending")) {
    			item.setStatus("sent");
    			cartItemService.saveCartItem(item);
    	    	emailService.sendMail(userService.getById(item.getProducer_id()).getEmail(),"You have a new Retail Order!","You have a new retail order. You can check it out at your 'Retail Orders' tab.");

    		}
    	}
    	
    	
    	emailService.sendMail(user.getEmail(),"Your orders have been sent!","All of the items from your cart have been sent as orders to the producers.You can review your orders at your 'My Orders' tab.");
    	
    	return "redirect:/homepage";
    }
    
    @GetMapping("/myorders")
	public String showOrders(Model model) {
		
		List<CartItem> claimedList_before = cartItemService.getUserCI();
		List<CartItem> claimedList = new ArrayList<>();
		
		for(CartItem item : claimedList_before) {
			if(!item.getStatus().equals("pending")) {
				claimedList.add(item); // 15/1
			}
		}
		
		model.addAttribute("claimedList", claimedList);
		List<Float> prices = new ArrayList<>();
		List<String> types = new ArrayList<>();
		List<String> addresses = new ArrayList<>();
		
		for (CartItem cartItem : claimedList) {
			RetailOffer offer = civilOfferService.findCOffer(cartItem.getCivil_offer_id());
			prices.add(offer.getPrice_per_kg());
			types.add(offer.getType_of_product());
			
			addresses.add(userService.getById(cartItem.getBuyer_id()).getAddress());
		}
		
		model.addAttribute("types", types);
		model.addAttribute("prices", prices);
		model.addAttribute("addresses", addresses);
		
		
		
		return "myOrders";
	}
    
    
}
