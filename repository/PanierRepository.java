package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Panier;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Long>{
	public Panier findByUserUid(Long userUid);
}
