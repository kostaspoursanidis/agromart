package com.controllers;


import java.util.ArrayList;
import java.util.List;

import com.model.RetailOffer;
import com.model.User;
import com.services.FruitService;
import com.services.RetailOfferService;
import com.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomepageController {

    @Autowired
    private RetailOfferService civilOfferService;

    @Autowired
    private UserService userService;

    @Autowired
    private FruitService fruitService;


    @GetMapping("/homepage")
	public String home(Model model) {

        List<RetailOffer> offerList = civilOfferService.getAllOffers();
        model.addAttribute("offer", offerList);
        model.addAttribute("userService", userService);   
        
		return "homepage";

	}

    @GetMapping("/homepage/filtered")
    public String filter(@RequestParam("filter") String type,@RequestParam(name="sorting",required=false) String sort,@RequestParam(name="maxPrice", required=false) Float maxPrice, Model model){
    	
    	List<RetailOffer> offerList = new ArrayList<RetailOffer>();
    	if(maxPrice == null) {
	    	if(sort == null) {
	    		 offerList = civilOfferService.getFiltered(type);
	    	}else if(sort.equals("ascending")) {
	    		 offerList = civilOfferService.getFiltered(type,sort);						
	    	}else {
	    		 offerList = civilOfferService.getFiltered(type,sort);
	    	}
    	}else {
    		if(sort == null) {
	    		 offerList = civilOfferService.getFiltered(type,maxPrice);
	    	}else if(sort.equals("ascending")) {
	    		 offerList = civilOfferService.getFiltered(type,sort,maxPrice);
	    	}else {
	    		 offerList = civilOfferService.getFiltered(type,sort,maxPrice);
	    	}
    	}
    	
    	
        model.addAttribute("offer", offerList);
        model.addAttribute("userService", userService);

        return "homepage";
    }


    @GetMapping("/homepage/producers")
    public String showProducers(Model model){

        List<User> producerList = userService.getAllProducers();
        model.addAttribute("producers", producerList);
        model.addAttribute("fruitserv", fruitService);
        model.addAttribute("currUser", userService.getCurrentSessionUser());
        model.addAttribute("find", "no");
        
        return "findproducers";
    }
    
    @GetMapping("/homepage/producers/filtered")
    public String showProducersFiltered(@RequestParam(name="produce",required=false) String produce,@RequestParam(name="rating",required=false) Float rating,
    		@RequestParam(name="name",required=false) String name, Model model){

        List<User> producerList = userService.getAllProducers();
        if(name.equals("")) {
        	
        	if(rating == null) {
        		producerList = userService.getFilteredByProduce(produce);
        	}else {
        		producerList = userService.getFilteredByProduceAndRating(produce,rating);
        	}
        	
        }else {
        	
        	if(rating == null) {
        		producerList = userService.getFilteredByProduceAndName(produce,name);
        	}else {
        		producerList = userService.getFilteredByProduceAndRatingAndName(produce,rating,name);
        	}
        }
        
        
    	
        model.addAttribute("producers", producerList);
        model.addAttribute("fruitserv", fruitService);
        model.addAttribute("currUser", userService.getCurrentSessionUser());
        model.addAttribute("find", "yes");
        
        return "findproducers";
    }

    @GetMapping("/homepage/buyers")
    public String showBuyers(Model model){

        List<User> buyerList = userService.getAllBuyers();
        model.addAttribute("buyers", buyerList);
        model.addAttribute("find", "no");
        
        
        return "findbuyers";
    }
    
    @GetMapping("/homepage/buyers/filtered")
    public String showBuyersFiltered(@RequestParam(name="rating",required=false) Float rating,
    		@RequestParam(name="name",required=false) String name, Model model){

        List<User>buyerList = userService.getAllBuyers();
        if(name.equals("")) {
        	if(rating != null) {
        		buyerList = userService.getBuyersFilteredByRating(rating);
        	}	
        }else {
        	if(rating == null) {
        		buyerList = userService.getBuyersFilteredByName(name);
        	}else {
        		buyerList = userService.getBuyersFilteredByRatingAndName(rating,name);
        	}
        }
        
        
    	
        model.addAttribute("buyers", buyerList);
        model.addAttribute("find", "yes");
        
        
        return "findbuyers";
    }
}
