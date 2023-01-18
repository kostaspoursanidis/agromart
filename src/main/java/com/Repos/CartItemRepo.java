package com.Repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.Model.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Long>{

	@Query("FROM CartItem o WHERE o.buyer_id=:p") 
	public List<CartItem> getCurrentUserCI(@Param("p") Long id);

	@Query("FROM CartItem o WHERE o.producer_id=:p" )  
	public List<CartItem> getCurrentUserCIProd(@Param("p") Long id);

}
