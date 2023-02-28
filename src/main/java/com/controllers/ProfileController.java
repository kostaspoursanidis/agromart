package com.controllers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.model.Follower;
import com.model.Fruit;
import com.model.Rating;
import com.model.User;
import com.services.FollowerService;
import com.services.FruitService;
import com.services.RatingService;
import com.services.UserService;
import com.utils.FileUploadUtil;

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

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
	private UserService userService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private FruitService fruitService;

    private String passHolder;
    private User userHolder;
    

    @GetMapping
    public String showProfile(Model model){
        User user = userService.getCurrentSessionUser();
        User curruser = userService.getCurrentSessionUser();
        model.addAttribute("user", user);
        model.addAttribute("curruser", curruser);

        //Showing Fruit List
        List<String> fruits = new ArrayList<String>();
        for (Fruit item : fruitService.getByProdID(user.getId())){
            fruits.add(item.getType());
        }
        model.addAttribute("fruitList", fruits);

        String address_for_q = user.getAddress().replace(" ", "%20");
        model.addAttribute("addressQ", address_for_q);


        return "profilePage";
    }

    @GetMapping("/follow")
    public String follow(){

        Follower followers = new Follower(userHolder.getId(),userService.getCurrentSessionUser().getId());
        followerService.saveFollower(followers);

        return "redirect:/profile/" + userHolder.getId();
    }

    @GetMapping("/unfollow")
    public String unfollow(){

        
        Follower followers = followerService.getFollower(userHolder.getId(), userService.getCurrentSessionUser().getId());
        followerService.deleteFollower(followers);

        return "redirect:/profile/" + userHolder.getId();
    }

    
    @GetMapping("/{id}")
    public String showOtherProfiles(@PathVariable("id") Long id,Model model){
        User user = userService.getById(id);
        model.addAttribute("user", user);
            
        User curruser = userService.getCurrentSessionUser();
        model.addAttribute("curruser", curruser);

        //Part of Following
        String isFollowed = "false";
        
        if(followerService.getFollower(user.getId(), curruser.getId())==null){
            model.addAttribute("isFollowed", isFollowed);
        }else{
            isFollowed="true";
            model.addAttribute("isFollowed", isFollowed);
        }

        //Showing Fruit List
        List<String> fruits = new ArrayList<String>();
        for (Fruit item : fruitService.getByProdID(user.getId())){
            fruits.add(item.getType());
        }
        model.addAttribute("fruitList", fruits);

        userHolder = user;

        String address_for_q = user.getAddress().replace(" ", "%20");
        model.addAttribute("addressQ", address_for_q);


        return "profilePage";
    }

    @GetMapping("/edit/{id}")
    public String editProfile(@PathVariable("id") Long id,Model model){
        
        User user = userService.getById(id);
        model.addAttribute("user", user);
        
        //To be tested
        if(user.getRole().equals("producer")) {
        	List<Fruit> fruitList = fruitService.getByProdID(id);
        	List<String> fruits = new ArrayList<>();
        	for(Fruit fruit: fruitList ) {
        		fruits.add(fruit.getType());
        	}
        	model.addAttribute("fruits", fruits);
        }

        passHolder=user.getPassword();
        
 
        return "profileEditForm";
    }

    @PostMapping("/edit/save")
    public String saveChanges(@ModelAttribute("user") User user,@RequestParam("image") MultipartFile multipartFile,@RequestParam(name = "idf",required = false) String[] fruits) throws IOException{

        
        Boolean passChanged = false;
        
        if(!user.getPassword().equals("")){
            passChanged = true;
        }else{
            user.setPassword(passHolder);
        }

        //Uploading of User Profile Photo
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
        if(!fileName.equals("")){
            
            user.setPhoto(fileName);
            String uploadDir = "src/main/resources/static/user-photos/" + user.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        
        //Adding or editing fruits
        if(fruits != null) {
	        if(fruits.length !=0){
	            fruitService.deleteByProdID(user.getId());
	            for (String fruit : fruits){
	                if(fruitService.hasFruit(user.getId(), fruit) == true){
	                    continue;
	                }else{
	                    Fruit newfruit = new Fruit(user.getId(),fruit);
	                    fruitService.saveFruitToUser(newfruit);
	                }
	            }
	        }
        }else {
        	fruitService.deleteByProdID(user.getId());
        }

        userService.editUser(user,passChanged);
        
        return "redirect:/profile";

    }

    @GetMapping("/rate/{id}")
    public String rateUser(@PathVariable("id") Long id,Model model){
        userHolder = userService.getById(id);
        model.addAttribute("id", id);
        model.addAttribute("user", userHolder);
        

        return "rateUser";
    }

    @PostMapping("/rate/save")
    public String saveRating(@RequestParam("rating") Float rating){

        User currentUser = userService.getCurrentSessionUser();

        User user = userHolder;
        
        if(ratingService.getSpecificUserRating(user.getId(),currentUser.getId()) == null){
            Rating newRating = new Rating(user.getId(),currentUser.getId(),rating);
            ratingService.save(newRating);
        }else{
            Rating oldRating = ratingService.getSpecificUserRating(user.getId(), currentUser.getId());
            oldRating.setRating(rating);
            ratingService.save(oldRating);
        }

        Float updatedRating = 0f;
        for(Rating item : ratingService.getAllUserRating(user.getId())){
            updatedRating = updatedRating + item.getRating();
        }
        
        updatedRating = updatedRating / ratingService.getAllUserRating(user.getId()).size();

        
        String dff = String.format("%.1f", updatedRating);
        dff = dff.replace(',', '.');
        dff = dff+"f";
        updatedRating = Float.parseFloat(dff);
        user.setRating(updatedRating);
        userService.editUser(user, false);
        userHolder=null;


        return "redirect:/profile/" + user.getId();
    }
    
    @GetMapping("/followerlist")
    public String manageFollowers(Model model) {
    	
    	User currUser = userService.getCurrentSessionUser();
    	
    	Long currID = currUser.getId();
    	
    	List<Follower> fList = followerService.getAllFollowees(currID);
    	
    	List<User> followee_list = new ArrayList<User>();
    	
    	for (Follower followee : fList) {
			Long f_id = followee.getFollowedUserID();
			followee_list.add(userService.getById(f_id));
		}
    	
    	model.addAttribute("followerList", followee_list);
    	
    	return "followerList";
    }


    
}
