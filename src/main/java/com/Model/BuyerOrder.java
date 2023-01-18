package com.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class BuyerOrder {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	private float price_per_kg;
	private String buyer_name;
	private String type_of_product;
	private float approx_kilos_wanted;
	private Long buyer_id;
	private Long producer_id;
	private String status;

	
	public BuyerOrder() {}
	
//	public BuyerOffer(float price_per_kg, String buyer_name, String type_of_product, float approx_kilos_wanted,Long buyer_id,Long producer_id) {
//		super();
//		this.price_per_kg = price_per_kg;
//		this.buyer_name = buyer_name;
//		this.type_of_product = type_of_product;
//		this.approx_kilos_wanted = approx_kilos_wanted;
//		this.buyer_id = buyer_id;
//		this.producer_id = producer_id;
//		
//	}
	
	

	public BuyerOrder(float price_per_kg, String buyer_name, String type_of_product, float approx_kilos_wanted,
			Long buyer_id, Long producer_id, String status) {
		super();
		this.price_per_kg = price_per_kg;
		this.buyer_name = buyer_name;
		this.type_of_product = type_of_product;
		this.approx_kilos_wanted = approx_kilos_wanted;
		this.buyer_id = buyer_id;
		this.producer_id = producer_id;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getPrice_per_kg() {
		return price_per_kg;
	}

	public void setPrice_per_kg(float price_per_kg) {
		this.price_per_kg = price_per_kg;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getType_of_product() {
		return type_of_product;
	}

	public void setType_of_product(String type_of_product) {
		this.type_of_product = type_of_product;
	}

	public float getApprox_kilos_wanted() {
		return approx_kilos_wanted;
	}

	public void setApprox_kilos_wanted(float approx_kilos_wanted) {
		this.approx_kilos_wanted = approx_kilos_wanted;
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
		this.producer_id=producer_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	
	

}
