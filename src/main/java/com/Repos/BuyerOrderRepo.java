package com.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.Model.BuyerOrder;


public interface BuyerOrderRepo extends JpaRepository< BuyerOrder, Long>{
    
    @Query("FROM BuyerOffer o WHERE o.buyer_id=:p")  
	public List<BuyerOrder> getCurrentUserBO(@Param("p") Long id);

    @Query("FROM BuyerOffer o WHERE o.producer_id=:p")  
	public List<BuyerOrder> getCurrentUserBOProd(@Param("p") Long id);

    @Query("FROM BuyerOffer o WHERE o.type_of_product=:p")
    public List<BuyerOrder> getFilteredBO(@Param("p") String type);
}
