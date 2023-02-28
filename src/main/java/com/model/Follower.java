package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Follower {

    @Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;

    private Long follow_user_id;
    private Long followee_id;

    public Follower(Long follow_user_id,Long followee_id){
        this.follow_user_id = follow_user_id;
        this.followee_id = followee_id;
    }

    public Follower(){}

    public Long getFolloweeID(){
        return followee_id;
    }
    public Long getFollowedUserID(){
        return follow_user_id;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setFollow_user_id(Long follow_user_id) {
		this.follow_user_id = follow_user_id;
	}

	public void setFollowee_id(Long followee_id) {
		this.followee_id = followee_id;
	}
    
    

    
}
