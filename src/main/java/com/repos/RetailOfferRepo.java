package com.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.RetailOffer;

public interface RetailOfferRepo extends JpaRepository<RetailOffer, Long>{
	
	@Query("FROM RetailOffer o WHERE o.producer_id=:p")  
	public List<RetailOffer> getCurrentUserCO(@Param("p") Long id);

	@Query("FROM RetailOffer o WHERE (:p = 'any' OR o.type_of_product= :p) ")  
	public List<RetailOffer> getFilteredCO(@Param("p") String type);
	
	@Query("FROM RetailOffer o WHERE (:p = 'any' OR o.type_of_product= :p) AND o.price_per_kg <= :m")  
	public List<RetailOffer> getFilteredCOMax(@Param("p") String type,@Param("m") Float maxPrice);
	
	@Query("FROM RetailOffer o WHERE (:p = 'any' OR o.type_of_product= :p) ORDER BY o.price_per_kg asc")  
	public List<RetailOffer> getFilteredCOAsc(@Param("p") String type);
	
	@Query("FROM RetailOffer o WHERE (:p = 'any' OR o.type_of_product= :p) ORDER BY o.price_per_kg desc")  
	public List<RetailOffer> getFilteredCODesc(@Param("p") String type);
	
	@Query("FROM RetailOffer o WHERE (:p = 'any' OR o.type_of_product= :p) AND o.price_per_kg <= :m ORDER BY o.price_per_kg asc")  
	public List<RetailOffer> getFilteredCOMaxAsc(@Param("p") String type,@Param("m") Float maxPrice);
	
	@Query("FROM RetailOffer o WHERE (:p = 'any' OR o.type_of_product= :p) AND o.price_per_kg <= :m ORDER BY o.price_per_kg desc")  
	public List<RetailOffer> getFilteredCOMaxDesc(@Param("p") String type,@Param("m") Float maxPrice);
	
}
