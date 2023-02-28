package com.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.Fruit;

public interface FruitRepo extends JpaRepository<Fruit,Long> {

    @Modifying
    @Query("DELETE FROM Fruit f WHERE f.prod_id =:o ")
    public void deleteByProdID(@Param("o") Long prod_id);

    @Query("FROM Fruit f WHERE f.prod_id = :o")
    public List<Fruit> getByProdID(@Param("o") Long prod_id);

    @Query("FROM Fruit f WHERE f.prod_id = :o AND f.type=:p")
    public Fruit hasFruit(@Param("o") Long prod_id,@Param("p") String fruit);

    
}
