package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.entities.Ligne_panier;
import com.example.ecommerce.entities.Utilisateur;
import com.example.ecommerce.service.Ligne_panierService;
@CrossOrigin(origins="http://localhost:4200")
@RestController
public class Ligne_panierController {

	@Autowired
	Ligne_panierService lpserv;
	
	@PostMapping(value = "/ajouterProduitAuPanier/{iduser}/{prodid}/{quantite}")
    public Ligne_panier ajouterProduitAuPanier(@PathVariable Long iduser,@PathVariable Long prodid,@PathVariable Long quantite) {
       return lpserv.ajouterProduitAuPanier(iduser, prodid, quantite);
    }
    @PutMapping(value = "/modifierQuantiteDansPanier/{idlignepan}/{nouvelleQuantite}")
    public Ligne_panier modifierQuantiteDansPanier(@PathVariable Long idlignepan,@PathVariable Long nouvelleQuantite) {
    	return lpserv.modifierQuantiteDansPanier(idlignepan, nouvelleQuantite);
    }
    @DeleteMapping(value = "/supprimerProduitDuPanier/{idlignePanier}")
    public String supprimerProduitDuPanier(@PathVariable Long idlignePanier) {
    	return lpserv.supprimerProduitDuPanier(idlignePanier);
    }
    @GetMapping(value = "/getLignesPanierUtilisateur")
    public List<Ligne_panier> getLignesPanierUtilisateur(@RequestBody Utilisateur utilisateur) {
        return lpserv.getLignesPanierUtilisateur(utilisateur);
    }

}
