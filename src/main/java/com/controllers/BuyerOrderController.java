package com.controllers;

import java.util.List;

import com.model.BuyerOrder;
import com.model.User;
import com.services.BuyerOrderService;
import com.services.EmailService;
import com.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/BuyerOffers")
public class BuyerOrderController {


    @Autowired
    private BuyerOrderService buyerOfferService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    

    @GetMapping
    public String showManagePage(Model model){

        User user = userService.getCurrentSessionUser();
        model.addAttribute("user", user);
        if(user.getRole().equals("producer")){
            List<BuyerOrder> userBOs = buyerOfferService.getUserBOProd();
		    model.addAttribute("userBOs", userBOs);
        }else{
            List<BuyerOrder> userBOs = buyerOfferService.getUserBO();
		    model.addAttribute("userBOs", userBOs);
        }

        return "buyerOffers";
    }


    @GetMapping("/delete/{id}")
	public String deleteOffer(@PathVariable("id") Long id){
		buyerOfferService.delete(id);
		return "redirect:/BuyerOffers";

	}

    @GetMapping("/edit/{id}")
	public String editOffer(@PathVariable("id") Long id,Model model){
		BuyerOrder oldco = buyerOfferService.findBOffer(id); 
		model.addAttribute("buyerOffer", oldco);
		
		
		return "buyerOfferCreation";
	}

    @GetMapping("/create/{id}")
    public String createBuyerOffer(@PathVariable("id") Long prod_id ,Model model){
        BuyerOrder buyerOffer = new BuyerOrder();
        buyerOffer.setProducer_id(prod_id);
        model.addAttribute("buyerOffer", buyerOffer);
        

        return "buyerOfferCreation";
    }

    @PostMapping("/create/save")
    public String saveBuyerOffer(@ModelAttribute("buyerOffer") BuyerOrder buyerOffer,@RequestParam("other")String other){

        User user = userService.getCurrentSessionUser();
        buyerOffer.setBuyer_id(user.getId());
        buyerOffer.setStatus("awaiting_response");
        buyerOffer.setBuyer_name(user.getName());

        if(buyerOffer.getType_of_product().equals("Other")){
			buyerOffer.setType_of_product(other);
		}
        
        String to = userService.getById(buyerOffer.getProducer_id()).getEmail();
        
        if(buyerOffer.getId() == null) {
        
        emailService.sendMail(to, "You have a new Wholesale Order" ,"A new Wholesale Order has been made. Go check it out at your Wholesale Orders Tab!");
        }
        buyerOfferService.saveOffer(buyerOffer);
        return "redirect:/BuyerOffers";
    }

    @GetMapping("/accept/{id}") 
    public String acceptBuyerOffer(@PathVariable("id") Long id){


        BuyerOrder offer = buyerOfferService.findBOffer(id);
        offer.setStatus("pending_payment");
        
        buyerOfferService.saveOffer(offer);

        emailService.sendMail(userService.getById(offer.getBuyer_id()).getEmail()
       , "You have an accepted offer!", "Please check your 'Wholesale Orders' tab for further information.");
     
        return "redirect:/BuyerOffers";
    }

    @GetMapping("/decline/{id}")
    public String declineBuyerOffer(Model model,@PathVariable("id") Long id){

        BuyerOrder offer = buyerOfferService.findBOffer(id);

        model.addAttribute("order", offer);

        return "declineForm";
    }
    
    @PostMapping("/decline/save")
    public String declineBuyerOfferPost(@ModelAttribute("order")BuyerOrder order,@RequestParam(name="reason",required=false) String reason){

        BuyerOrder ret_order = buyerOfferService.findBOffer(order.getId());
    	
    	emailService.sendMail(userService.getById(ret_order.getBuyer_id()).getEmail()
        , "Your order has been declined!", "We're sorry to inform you that one of the orders you've made has been declined!The provided reason from the producer is : " + reason);

        buyerOfferService.delete(order.getId());
        
        return "redirect:/BuyerOffers";
    }
    
    @GetMapping("/depositMade/{id}") 
    public String depositMade(@PathVariable("id") Long id){

        BuyerOrder offer = buyerOfferService.findBOffer(id);
        offer.setStatus("awaiting_completion");
        buyerOfferService.saveOffer(offer);


        emailService.sendMail(userService.getById(offer.getProducer_id()).getEmail() 
        , "A payment has been made!", "A payment has been made for one of your Wholesale Orders. For further information go to your 'Wholesale Orders' tab.");

        return "redirect:/BuyerOffers";
    }
    
    @GetMapping("/confirmDeposit/{id}") 
    public String confirmDeposit(@PathVariable("id") Long id){

        BuyerOrder offer = buyerOfferService.findBOffer(id);
        offer.setStatus("completed");
        buyerOfferService.saveOffer(offer);


        emailService.sendMail(userService.getById(offer.getBuyer_id()).getEmail()
        , "Your payment has been aknowledged", "Your payment has been aknowledged for one of your Wholesale Orders.All you have to do now is wait for your goods to arrive!");

        return "redirect:/BuyerOffers";
    }
        
}
