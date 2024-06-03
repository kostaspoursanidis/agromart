package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class CartItem {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	private Long buyer_id;
	private Long producer_id;
	private Long retail_offer_id;
	private float kilos_wanted;
	private String status;
	
	public CartItem() {}
	
	public CartItem(Long buyer_id, Long producer_id, Long retail_offer_id, float kilos_wanted, String status) {
		super();
		this.buyer_id = buyer_id;
		this.producer_id = producer_id;
		this.retail_offer_id = retail_offer_id;
		this.kilos_wanted = kilos_wanted;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(Long buyer_id) {
		this.buyer_id = buyer_id;
	}

	public Long getProducer_id() {
		return producer_id;
	}

	public void setProducer_id(Long producer_id) {
		this.producer_id = producer_id;
	}

	public Long getCivil_offer_id() {
		return retail_offer_id;
	}

	public void setCivil_offer_id(Long civil_offer_id) {
		this.retail_offer_id = civil_offer_id;
	}

	public float getKilos_wanted() {
		return kilos_wanted;
	}

	public void setKilos_wanted(float kilos_wanted) {
		this.kilos_wanted = kilos_wanted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
	

}
