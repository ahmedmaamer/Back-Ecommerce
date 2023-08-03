package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entities.Livraison;
import com.example.ecommerce.entities.Paiement;


public interface InterPaiementService {
	public Paiement addPaiement(Paiement pai);
	
    public String deletePaiement(Long id);
    
    public List<Paiement> getAllPaiement();
    
    public Paiement updatePaiement(Paiement pay,Long id);
    
    public Paiement addLivraisonToPaiement(Livraison liv,Long idpai);
}
