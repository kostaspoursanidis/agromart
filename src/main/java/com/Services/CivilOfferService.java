package com.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.CivilOffer;
import com.Repos.CivilOfferRepo;

@Service
public class CivilOfferService {
	
	
	@Autowired
	private CivilOfferRepo civilOfferRepo;
	
	@Autowired
	private UserServiceImpl userService;
	
	
	public CivilOffer saveOffer(CivilOffer offer) {
		return civilOfferRepo.save(offer);    //Was void changed to return CIvilOffer 18/7/22
	}
	
	public List<CivilOffer> getUserCO(){
		
		Long currentUserID = userService.getCurrentSessionUser().getId();
		
		List<CivilOffer> userCOs = civilOfferRepo.getCurrentUserCO(currentUserID);
	
		return userCOs;
		
	}

	public void delete(Long id){
		civilOfferRepo.deleteById(id);
	}

	public CivilOffer findCOffer(Long id){
		CivilOffer co = civilOfferRepo.findById(id).orElse(null);
		return co;

	}

	public List<CivilOffer> getAllOffers(){
		return civilOfferRepo.findAll();
	}

	public List<CivilOffer> getFiltered(String type){
		return civilOfferRepo.getFilteredCO(type);
	}
	
	
	
	

}
