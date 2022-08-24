package com.Repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Model.CivilOffer;

public interface CivilOfferRepo extends JpaRepository<CivilOffer, Long>{
	
	@Query("FROM CivilOffer o WHERE o.producer_id=:p")  
	public List<CivilOffer> getCurrentUserCO(@Param("p") Long id);

	@Query("FROM CivilOffer o WHERE o.type_of_product=:p")  
	public List<CivilOffer> getFilteredCO(@Param("p") String type);
	
}
