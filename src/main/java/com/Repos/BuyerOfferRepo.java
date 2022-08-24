package com.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.Model.BuyerOffer;


public interface BuyerOfferRepo extends JpaRepository< BuyerOffer, Long>{
    
    @Query("FROM BuyerOffer o WHERE o.buyer_id=:p")  
	public List<BuyerOffer> getCurrentUserBO(@Param("p") Long id);

    @Query("FROM BuyerOffer o WHERE o.producer_id=:p")  
	public List<BuyerOffer> getCurrentUserBOProd(@Param("p") Long id);
}
