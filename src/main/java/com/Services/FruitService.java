package com.Services;

import java.util.List;

import com.Model.Fruits;
import com.Repos.FruitRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FruitService {

    @Autowired
    private FruitRepo fruitRepo;

    @Transactional
    public void deleteByProdID(Long prod_id){
        fruitRepo.deleteByProdID(prod_id);
    }

    public List<Fruits> getByProdID(Long prod_id){
        return fruitRepo.getByProdID(prod_id);
    }

    public Boolean hasFruit(Long prod_id,String fruit){
        
        if(fruitRepo.hasFruit(prod_id, fruit)!= null){
            return true;
        }else{
            return false;
        }
        
    }

    public void saveFruitToUser(Fruits fruit){
        fruitRepo.save(fruit);
    }
    

    
}
