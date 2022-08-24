package com.Dto;


public class UserDto {
	
	private String name;
	private String email;
	private String password;
	private String role;
	private String address;
	private String[] products;
	private String phoneNum;
	private float rating;
	
	public UserDto(String name ,String email, String password, String role, String address,String phoneNum) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.address = address;
		this.phoneNum = phoneNum;
	}

	public UserDto() {
		
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

	public String[] getProducts() {
		return products;
	}

	public void setProducts(String[] products) {
		this.products = products;
	}

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
	
	

}
