package com.Services;

import java.util.List;
import com.Repos.BuyerOrderRepo;
import com.Model.BuyerOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerOrderService {

    @Autowired
	private BuyerOrderRepo buyerOfferRepo;
	
	@Autowired
	private UserService userService;
	
	
	public void saveOffer(BuyerOrder offer) {
		buyerOfferRepo.save(offer);
	}
	
	public List<BuyerOrder> getUserBO(){
		
		Long currentUserID = userService.getCurrentSessionUser().getId();
		
		List<BuyerOrder> userBOs = buyerOfferRepo.getCurrentUserBO(currentUserID);
	
		return userBOs;
		
	}

    public List<BuyerOrder> getUserBOProd(){
		
		Long currentUserID = userService.getCurrentSessionUser().getId();
		
		List<BuyerOrder> userBOs = buyerOfferRepo.getCurrentUserBOProd(currentUserID);
	
		return userBOs;
		
	}

	public void delete(Long id){
		buyerOfferRepo.deleteById(id);
	}

	public BuyerOrder findBOffer(Long id){
		BuyerOrder co = buyerOfferRepo.findById(id).orElse(null);
		return co;

	}

	public List<BuyerOrder> getAllOffers(){
		return buyerOfferRepo.findAll();
	}

	public List<BuyerOrder> getFiltered(String type){
		return buyerOfferRepo.getFilteredBO(type) ;
	}

	
    
}
