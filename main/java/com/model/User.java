package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name =  "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	
	private String name;
	private String email;
	private String password;
	private String role;
	private String address;
	private String phoneNum;
	private String photo;
	
	private float rating;
	
	public User() {}
	
	public User(String name, String email, String password, String role, String address,
			 String phoneNum,String photo) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.address = address;
		//this.products = products;
		this.phoneNum = phoneNum;
		this.photo = photo;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public String[] getProducts() {
//		return products;
//	}
//
//	public void setProducts(String[] products) {
//		this.products = products;
//	}



	public String getPhoneNum() {
		return phoneNum;
	}



	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}



	public float getRating() {
		return rating;
	}



	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getPhoto() {

		if (photo == null || id == null) return null;
         
        return photo;
		
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	
	
	

}
