package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Paiement;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long>{

}
