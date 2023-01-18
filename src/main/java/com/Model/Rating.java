package com.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table
public class Rating {
    
    @Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;

    private Long rated_user_id;
    private Long author_id;


    private Float rating;

    public Rating(Long rated_user_id,Long author_id,Float rating){
        this.rated_user_id = rated_user_id;
        this.author_id = author_id;
        this.rating = rating;
    }

    public Rating(){}


    public Float getRating(){
        return rating;
    }
    
    public void setRating(Float rating){
        this.rating= rating;
    }

    public Long getAuthorId(){
        return author_id;
    }

    public Long getRatedUserId(){
        return rated_user_id;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setRated_user_id(Long rated_user_id) {
		this.rated_user_id = rated_user_id;
	}

	public void setAuthor_id(Long author_id) {
		this.author_id = author_id;
	}
    
    

    
}
