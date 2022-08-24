package com.Controllers;

import java.io.IOException;
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

import com.Model.CivilOffer;
import com.Model.Followers;
import com.Model.User;
import com.Services.CivilOfferService;
import com.Services.EmailServiceImpl;
import com.Services.FollowerService;
import com.Services.UserServiceImpl;
import com.Utils.FileUploadUtil;

@Controller
@RequestMapping("/CivilOffers")
public class CivilOfferController {
	
	@Autowired
	private CivilOfferService civilOfferService;
	

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private EmailServiceImpl emailService;

	@Autowired
	private FollowerService followerService;
	
	@GetMapping("/create")
	public String showCOCreationPage(Model model) {
		model.addAttribute("civiloffer", new CivilOffer());
		
		return "civilOfferCreationPage";
	}
	

	@PostMapping("/create/save")
	public String createOffer(@ModelAttribute("civiloffer") CivilOffer civilOffer,@RequestParam("other")String other,@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		User user = userService.getCurrentSessionUser();
		civilOffer.setProducer_id(user.getId());

		if(civilOffer.getType_of_product().equals("Other")){
			civilOffer.setType_of_product(other);
		}

		

		
		CivilOffer savedOffer = civilOfferService.saveOffer(civilOffer);

		//Uploading of Offer Photo
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if(!fileName.equals("")){
            savedOffer.setPhoto(fileName);
            String uploadDir = "src/main/resources/static/offer-photos/" + savedOffer.getId(); 
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

		String userName = user.getName();
		for ( Followers follower : followerService.getAllFollowers(user.getId())) {
			String address_to = userService.getById(follower.getFolloweeID()).getEmail();
			emailService.sendMail(address_to,userName+"just posted or edited an offer!","Hey there, check out the offer here:");//maybe add the link to the offer?
			
		}

		civilOfferService.saveOffer(savedOffer);
		
		return "redirect:/CivilOffers";
	}
	
	@GetMapping
	public String showCOList(Model model) {
		
		List<CivilOffer> userCOs = civilOfferService.getUserCO();
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
		CivilOffer oldco = civilOfferService.findCOffer(id);
		model.addAttribute("civiloffer", oldco);
		
		
		return "civilOfferCreationPage";
	}
}
