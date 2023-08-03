package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Ligne_panier;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.entities.Produit;
import com.example.ecommerce.entities.Utilisateur;

@Repository
public interface Ligne_panierRepository extends JpaRepository<Ligne_panier, Long> {

    Ligne_panier findByPanierAndProd(Panier panier,Produit prod);

    List<Ligne_panier> findByPanierUser(Utilisateur user);
}
