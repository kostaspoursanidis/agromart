package com.Repos;

import java.util.List;

import com.Model.Fruits;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FruitRepo extends JpaRepository<Fruits,Long> {

    @Modifying
    @Query("DELETE FROM Fruits f WHERE f.prod_id =:o ")
    public void deleteByProdID(@Param("o") Long prod_id);

    @Query("FROM Fruits f WHERE f.prod_id = :o")
    public List<Fruits> getByProdID(@Param("o") Long prod_id);

    @Query("FROM Fruits f WHERE f.prod_id = :o AND f.type=:p")
    public Fruits hasFruit(@Param("o") Long prod_id,@Param("p") String fruit);

    
}
