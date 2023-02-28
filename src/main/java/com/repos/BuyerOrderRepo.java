package com.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.BuyerOrder;

import java.util.List;


public interface BuyerOrderRepo extends JpaRepository< BuyerOrder, Long>{
    
    @Query("FROM BuyerOrder o WHERE o.buyer_id=:p")  
	public List<BuyerOrder> getCurrentUserBO(@Param("p") Long id);

    @Query("FROM BuyerOrder o WHERE o.producer_id=:p")  
	public List<BuyerOrder> getCurrentUserBOProd(@Param("p") Long id);

    @Query("FROM BuyerOrder o WHERE o.type_of_product=:p")
    public List<BuyerOrder> getFilteredBO(@Param("p") String type);
}
