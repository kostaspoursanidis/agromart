package com.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Model.CartItem;
import com.Model.RetailOffer;
import com.Model.Followers;
import com.Model.User;
import com.Services.CartItemService;
import com.Services.RetailOfferService;
import com.Services.EmailService;
import com.Services.FollowerService;
import com.Services.UserService;
import com.Utils.FileUploadUtil;

@Controller
@RequestMapping("/CivilOffers")
public class RetailOfferController {
	
	@Autowired
	private RetailOfferService civilOfferService;
	

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private FollowerService followerService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@GetMapping("/create")
	public String showCOCreationPage(Model model) {
		model.addAttribute("civiloffer", new RetailOffer());
		
		return "civilOfferCreationPage";
	}
	

	@PostMapping("/create/save")
	public String createOffer(@ModelAttribute("civiloffer") RetailOffer civilOffer,@RequestParam("other")String other,@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		User user = userService.getCurrentSessionUser();
		civilOffer.setProducer_id(user.getId());

		if(civilOffer.getType_of_product().equals("Other")){
			civilOffer.setType_of_product(other);
		}

		

		
		RetailOffer savedOffer = civilOfferService.saveOffer(civilOffer);

		//Uploading of Offer Photo
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if(!fileName.equals("")){
            savedOffer.setPhoto(fileName);
            String uploadDir = "src/main/resources/static/offer-photos/" + savedOffer.getId(); 
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

		/*String userName = user.getName();
		for ( Followers follower : followerService.getAllFollowers(user.getId())) {
			String address_to = userService.getById(follower.getFolloweeID()).getEmail();
			emailService.sendMail(address_to,userName+" just posted or edited an offer!","Hey there, check out the offer here:");//maybe add the link to the offer?
			
		}*/

		civilOfferService.saveOffer(savedOffer);
		
		return "redirect:/CivilOffers";
	}
	
	@GetMapping
	public String showCOList(Model model) {
		
		List<RetailOffer> userCOs = civilOfferService.getUserCO();
		model.addAttribute("userCOs", userCOs);
		
		return "civilOffers";
		
	}

	@GetMapping("/delete/{id}")
	public String deleteOffer(@PathVariable("id") Long id){
		civilOfferService.delete(id);
		return "redirect:/CivilOffers";

	}

	@GetMapping("/edit/{id}")
	public String editOffer(@PathVariable("id") Long id,Model model){
		RetailOffer oldco = civilOfferService.findCOffer(id);
		model.addAttribute("civiloffer", oldco);
		
		
		return "civilOfferCreationPage";
	}
	
	@GetMapping("/claimedOffers")
	public String showClaimedOffers(Model model) {
		
		List<CartItem> claimedList_before = cartItemService.getUserCIProd();
		List<CartItem> claimedList = new ArrayList<>(); // 15/1 
		
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
		
		
		
		return "claimed";
	}
	
	@GetMapping("/completed/{id}")
	public String setStatusComplete(@PathVariable("id") Long item_id){
		CartItem cartItem = cartItemService.findCartItemByID(item_id);
		cartItem.setStatus("completed");
		cartItemService.saveCartItem(cartItem);
		//send mail to customer here
		
		return "redirect:/CivilOffers/claimedOffers";
	}
}
