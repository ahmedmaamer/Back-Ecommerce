package com.example.ecommerce.service;


import java.util.List;

import com.example.ecommerce.entities.Ligne_panier;
import com.example.ecommerce.entities.Utilisateur;

public interface InterLigne_panierService {

	public Ligne_panier ajouterProduitAuPanier(Long iduser, Long prodid, Long quantite);
	
	public Ligne_panier modifierQuantiteDansPanier(Long idlignepan, Long nouvelleQuantite);
	
	public String supprimerProduitDuPanier(Long idlignePanier);
	
    public List<Ligne_panier> getLignesPanierUtilisateur(Utilisateur utilisateur);
   
}
