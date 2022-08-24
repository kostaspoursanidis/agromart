package com.Services;

import java.util.List;
import com.Repos.BuyerOfferRepo;
import com.Model.BuyerOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerOfferService {

    @Autowired
	private BuyerOfferRepo buyerOfferRepo;
	
	@Autowired
	private UserServiceImpl userService;
	
	
	public void saveOffer(BuyerOffer offer) {
		buyerOfferRepo.save(offer);
	}
	
	public List<BuyerOffer> getUserBO(){
		
		Long currentUserID = userService.getCurrentSessionUser().getId();
		
		List<BuyerOffer> userBOs = buyerOfferRepo.getCurrentUserBO(currentUserID);
	
		return userBOs;
		
	}

    public List<BuyerOffer> getUserBOProd(){
		
		Long currentUserID = userService.getCurrentSessionUser().getId();
		
		List<BuyerOffer> userBOs = buyerOfferRepo.getCurrentUserBOProd(currentUserID);
	
		return userBOs;
		
	}

	public void delete(Long id){
		buyerOfferRepo.deleteById(id);
	}

	public BuyerOffer findBOffer(Long id){
		BuyerOffer co = buyerOfferRepo.findById(id).orElse(null);
		return co;

	}

	public List<BuyerOffer> getAllOffers(){
		return buyerOfferRepo.findAll();
	}

	
    
}
