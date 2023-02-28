package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class RetailOffer {
	
		
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
		
	private float price_per_kg;
	private Long producer_id;
	private String type_of_product;
	private String offer_text;
	private String title;
	private String photo;


	public RetailOffer(float price_per_kg, Long producer_id, String type_of_product,String offer_text,String title,String photo) {
		super();
		this.price_per_kg = price_per_kg;
		this.producer_id = producer_id;
		this.type_of_product = type_of_product;
		this.offer_text = offer_text;
		this.title = title;
		this.photo = photo;
	}
	
	public RetailOffer(){}
	
	public String getOffer_text() {
		return offer_text;
	}


	public void setOffer_text(String offer_text) {
		this.offer_text = offer_text;
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


	public Long getProducer_id() {
		return producer_id;
	}


	public void setProducer_id(Long producer_id) {
		this.producer_id = producer_id;
	}


	public String getType_of_product() {
		return type_of_product;
	}


	public void setType_of_product(String type_of_product) {
		this.type_of_product = type_of_product;
	}

	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}
	

}
