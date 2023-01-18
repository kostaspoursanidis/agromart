package com.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Fruits {

    @Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;

    private Long prod_id;
    private String type;

    public Fruits(Long prod_id,String type){
        this.prod_id = prod_id;
        this.type = type;
    }

    public Fruits(){};

    public String getType(){
        return type;
    }

    public Long getProd_id(){
        return prod_id;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setProd_id(Long prod_id) {
		this.prod_id = prod_id;
	}

	public void setType(String type) {
		this.type = type;
	}

    
    
}
