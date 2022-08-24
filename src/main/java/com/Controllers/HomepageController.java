package com.Controllers;


import java.util.List;

import com.Model.CivilOffer;
import com.Model.User;
import com.Services.CivilOfferService;
import com.Services.FruitService;
import com.Services.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomepageController {

    @Autowired
    private CivilOfferService civilOfferService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FruitService fruitService;


    @GetMapping("/homepage")
	public String home(Model model) {

        List<CivilOffer> offerList = civilOfferService.getAllOffers();
        model.addAttribute("offer", offerList);
        model.addAttribute("userService", userService);   
        
		return "homepage";

	}

    @GetMapping("/homepage/filtered")
    public String filter(@RequestParam("filter") String type, Model model){

        

        List<CivilOffer> offerList = civilOfferService.getFiltered(type);
        model.addAttribute("offer", offerList);
        model.addAttribute("userService", userService);

        return "homepage";
    }


    @GetMapping("/homepage/producers")
    public String showProducers(Model model){

        

        List<User> producerList = userService.getAllUsers();
        model.addAttribute("producers", producerList);

        model.addAttribute("fruitserv", fruitService);
        
        return "findproducers";
    }

    
}
