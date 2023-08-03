package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entities.Ligne_panier;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.entities.Utilisateur;

public interface InterPanierService {
	
	public Panier addpanier(Panier pan);
 
    public List<Panier> getAllPanier();
    
    public void removeUserFromPanier(Long idp);
    
    public Panier getPanierByUser(Long iduser);
    
    public Panier getPanierById(Long idPan);
    
    public Utilisateur getUserByPanier(Long idPan);
    
    public float calculerMontantTotalPanier(Long iduser);
    
    public Panier addLignepanierToPanier(Ligne_panier lp);
    
}
