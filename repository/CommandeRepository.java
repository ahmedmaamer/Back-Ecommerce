package com.example.ecommerce.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long>{
    public List<Commande> getAllByPanIdpanier(Long panIdpanier);
}
