package com.Controllers;

import java.util.List;

import com.Model.BuyerOffer;
import com.Model.User;
import com.Services.BuyerOfferService;
import com.Services.EmailServiceImpl;
import com.Services.UserServiceImpl;

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
public class BuyerOfferController {


    @Autowired
    private BuyerOfferService buyerOfferService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EmailServiceImpl emailService;

    

    @GetMapping
    public String showManagePage(Model model){

        User user = userService.getCurrentSessionUser();
        model.addAttribute("user", user);
        if(user.getRole().equals("producer")){
            List<BuyerOffer> userBOs = buyerOfferService.getUserBOProd();
		    model.addAttribute("userBOs", userBOs);
        }else{
            List<BuyerOffer> userBOs = buyerOfferService.getUserBO();
		    model.addAttribute("userBOs", userBOs);
        }

        return "buyerOffers";
    }

    // Idea : Check in the Controller if the current user is producer or buyer and return 2 diff methods based on an if on the actual GetMapping

    // @GetMapping("/create")
    // public String createBuyerOffer(Model model){
    //     model.addAttribute("buyerOffer", new BuyerOffer());

    //     return "buyerOfferCreation";
    // }

    // @PostMapping("/create/save")
	// public String createOffer(@ModelAttribute("buyerOffer") BuyerOffer buyerOffer) {
		
	// 	User user = userService.getCurrentSessionUser();
	// 	buyerOffer.setBuyer_id(user.getId());
		
	// 	buyerOfferService.saveOffer(buyerOffer);		
		
	// 	return "redirect:/BuyerOffers";
	// }

    @GetMapping("/delete/{id}")
	public String deleteOffer(@PathVariable("id") Long id){
		buyerOfferService.delete(id);
		return "redirect:/BuyerOffers";

	}

    @GetMapping("/edit/{id}")
	public String editOffer(@PathVariable("id") Long id,Model model){
		BuyerOffer oldco = buyerOfferService.findBOffer(id); 
		model.addAttribute("buyerOffer", oldco);
		
		
		return "buyerOfferCreation";
	}

    @GetMapping("/create/{id}")
    public String createBuyerOffer(@PathVariable("id") Long prod_id ,Model model){
        BuyerOffer buyerOffer = new BuyerOffer();
        buyerOffer.setProducer_id(prod_id);
        model.addAttribute("buyerOffer", buyerOffer);
        

        return "buyerOfferCreation";
    }

    @PostMapping("/create/{id}/save")
    public String saveBuyerOffer(@ModelAttribute("buyerOffer") BuyerOffer buyerOffer,@RequestParam("other")String other){

        User user = userService.getCurrentSessionUser();
        buyerOffer.setBuyer_id(user.getId());
        //buyerOffer.setState(false);
        buyerOffer.setBuyer_name(user.getName());

        if(buyerOffer.getType_of_product().equals("Other")){
			buyerOffer.setType_of_product(other);
		}

        

        buyerOfferService.saveOffer(buyerOffer);
        return "redirect:/BuyerOffers";
    }

    @GetMapping("/accept/{id}") 
    public String acceptBuyerOffer(@PathVariable("id") Long id){


        BuyerOffer offer = buyerOfferService.findBOffer(id);
        offer.setState(true);

        //Send Email here
        
        buyerOfferService.saveOffer(offer);


        emailService.sendMail(userService.getById(offer.getBuyer_id()).getEmail()
        , "You have an accepted offer!", "Please check your 'Buyer Offer' page for further information.");
    

        //Maybe create a new form for the producer to tell the buyer info about dates & paradosi prointwn

 
        return "redirect:/BuyerOffers";
    }

    @GetMapping("/decline/{id}")
    public String declineBuyerOffer(@PathVariable("id") Long id){

        BuyerOffer offer = buyerOfferService.findBOffer(id);
        offer.setState(false);

        //Send Email here
        
        buyerOfferService.saveOffer(offer);

        emailService.sendMail(userService.getById(offer.getBuyer_id()).getEmail()
        , "Your offer has been declined!", "We're sorry to inform you that one of the offers you've made has been declined!");

        //Just delete offer and send notification (Ask Zarras) 
        //buyerOfferService.delete(id);
        //Maybe delete it from the DB and just Inform the Buyer?

        return "redirect:/BuyerOffers";
    }

    
}
