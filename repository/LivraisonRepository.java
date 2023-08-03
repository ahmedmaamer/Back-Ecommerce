package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Livraison;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long>{

}
