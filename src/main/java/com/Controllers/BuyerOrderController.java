package com.Controllers;

import java.util.List;

import com.Model.BuyerOrder;
import com.Model.User;
import com.Services.BuyerOrderService;
import com.Services.EmailService;
import com.Services.UserService;

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

        

        buyerOfferService.saveOffer(buyerOffer);
        return "redirect:/BuyerOffers";
    }

    @GetMapping("/accept/{id}") 
    public String acceptBuyerOffer(@PathVariable("id") Long id){


        BuyerOrder offer = buyerOfferService.findBOffer(id);
        offer.setStatus("pending_payment");

        //Send Email here
        
        buyerOfferService.saveOffer(offer);


        emailService.sendMail(userService.getById(offer.getBuyer_id()).getEmail()
       , "You have an accepted offer!", "Please check your 'Buyer Offer' page for further information.");
    

        

 
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

        emailService.sendMail(userService.getById(order.getBuyer_id()).getEmail()
        , "Your order has been declined!", "We're sorry to inform you that one of the orders you've made has been declined!The provided reason from the producer is : " + reason);

        buyerOfferService.delete(order.getId());
        
        return "redirect:/BuyerOffers";
    }
    
    @GetMapping("/depositMade/{id}") 
    public String depositMade(@PathVariable("id") Long id){

        BuyerOrder offer = buyerOfferService.findBOffer(id);
        offer.setStatus("awaiting_completion");
        buyerOfferService.saveOffer(offer);


//        emailService.sendMail(userService.getById(offer.getBuyer_id()).getEmail() //watch out who you send the mail to
//        , "You have an accepted offer!", "Please check your 'Buyer Offer' page for further information.");

        return "redirect:/BuyerOffers";
    }
    
    @GetMapping("/confirmDeposit/{id}") 
    public String confirmDeposit(@PathVariable("id") Long id){

        BuyerOrder offer = buyerOfferService.findBOffer(id);
        offer.setStatus("completed");
        buyerOfferService.saveOffer(offer);


//        emailService.sendMail(userService.getById(offer.getBuyer_id()).getEmail()
//        , "You have an accepted offer!", "Please check your 'Buyer Offer' page for further information.");

        return "redirect:/BuyerOffers";
    }
    
    
    
    
    
    
    
    
    //TODO method for clearing offers both here and to retail orders
    
    
    
}
