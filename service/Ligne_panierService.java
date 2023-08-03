package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entities.Ligne_panier;
import com.example.ecommerce.entities.Produit;
import com.example.ecommerce.entities.Utilisateur;
import com.example.ecommerce.repository.Ligne_panierRepository;
import com.example.ecommerce.repository.ProduitRepository;
import com.example.ecommerce.repository.UtilisateurRepository;

@Service
public class Ligne_panierService implements InterLigne_panierService{

	@Autowired
    ProduitRepository prodrep;
    @Autowired
    Ligne_panierRepository Lignepanierrep;

    @Autowired
    UtilisateurRepository userrep;
    
    @Override
    public Ligne_panier ajouterProduitAuPanier(Long iduser, Long prodid, Long quantite) {
        Produit produit = prodrep.findById(prodid).get();
        Utilisateur us = userrep.findById(iduser).get();
        Ligne_panier lignePanierExistante = Lignepanierrep.findByPanierAndProd(us.getP(), produit);

        if (lignePanierExistante != null) {
            lignePanierExistante.setQteAjoute(lignePanierExistante.getQteAjoute() + quantite);
            return lignePanierExistante;
        } else {
        	Ligne_panier nouvelleLignePanier = new Ligne_panier(us.getP(), produit, quantite);
           return Lignepanierrep.save(nouvelleLignePanier);
        }
    }
    @Override
    public Ligne_panier modifierQuantiteDansPanier(Long idlignepan, Long nouvelleQuantite) {
    	Ligne_panier lignePanier=Lignepanierrep.findById(idlignepan).get();
    	lignePanier.setQteAjoute(nouvelleQuantite);
       return Lignepanierrep.save(lignePanier);
    }
    
    @Override
    public String supprimerProduitDuPanier(Long idlignePanier) {
    	String ch="";
    	Lignepanierrep.deleteById(idlignePanier);
    	ch = "lignepanier deleted successfuly !!!";
    	return ch;
    }
    
    @Override
    public List<Ligne_panier> getLignesPanierUtilisateur(Utilisateur utilisateur) {
        return Lignepanierrep.findByPanierUser(utilisateur);
    }
    
}
