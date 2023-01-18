package com.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.RetailOffer;
import com.Repos.RetailOfferRepo;

@Service
public class RetailOfferService {
	
	
	@Autowired
	private RetailOfferRepo civilOfferRepo;
	
	@Autowired
	private UserService userService;
	
	
	public RetailOffer saveOffer(RetailOffer offer) {
		return civilOfferRepo.save(offer);    //Was void changed to return CIvilOffer 18/7/22
	}
	
	public List<RetailOffer> getUserCO(){
		
		Long currentUserID = userService.getCurrentSessionUser().getId();
		
		List<RetailOffer> userCOs = civilOfferRepo.getCurrentUserCO(currentUserID);
	
		return userCOs;
		
	}

	public void delete(Long id){
		civilOfferRepo.deleteById(id);
	}

	public RetailOffer findCOffer(Long id){
		RetailOffer co = civilOfferRepo.findById(id).orElse(null);
		return co;

	}

	public List<RetailOffer> getAllOffers(){
		return civilOfferRepo.findAll();
	}

	public List<RetailOffer> getFiltered(String type){
		return civilOfferRepo.getFilteredCO(type);
	}
	
	public Long getCOProd_id(Long co_id) {
		
		RetailOffer co = civilOfferRepo.getById(co_id);
		
		Long id = co.getProducer_id();
		
		return id;
	}
	
	public List<RetailOffer> getFiltered(String type,String sort){
		if(sort.equals("ascending")) {
			return civilOfferRepo.getFilteredCOAsc(type);
		}else {
			return civilOfferRepo.getFilteredCODesc(type);
		}
	}
	
	public List<RetailOffer> getFiltered(String type,String sort,Float maxPrice){
		if(sort.equals("ascending")) {
			return civilOfferRepo.getFilteredCOMaxAsc(type,maxPrice);
		}else {
			return civilOfferRepo.getFilteredCOMaxDesc(type,maxPrice);
		}
	}
	
	public List<RetailOffer> getFiltered(String type,Float maxPrice){

		return civilOfferRepo.getFilteredCOMax(type,maxPrice);

	}
	
	
	

}
