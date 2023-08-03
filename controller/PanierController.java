package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.entities.Ligne_panier;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.entities.Utilisateur;
import com.example.ecommerce.service.PanierService;
@CrossOrigin(origins="http://localhost:4200")
@RestController
public class PanierController {
     @Autowired
     PanierService panserv;
     
     @GetMapping(value = "/getallpanier")
     public List<Panier> getAllPanier() {
 		
 		return panserv.getAllPanier();
 	 }
     @DeleteMapping(value = "/removeUserFromPanier/idp")
     public void deletePanier(@PathVariable Long idp) {
 		 panserv.removeUserFromPanier(idp);
 	}
     @GetMapping(value = "/getpanierbyid/{idpan}")
     public Panier getPanierById(@PathVariable Long idpan) {
 		
 		return panserv.getPanierById(idpan);
 	 }
     @GetMapping(value = "/getuserbypanier/{idpan}")
     public Utilisateur getUserByPanier(@PathVariable Long idpan) {
    	 return panserv.getUserByPanier(idpan);
 	}
     @GetMapping(value = "/getPanierByiduser/{iduser}")
     public Panier getPanierByIduser(@PathVariable Long iduser) {
 			return panserv.getPanierByUser(iduser);
 	}
     @GetMapping(value = "/calculerMontantTotalPanier/{iduser}")
     public float calculerMontantTotalPanier(@PathVariable Long iduser) {
       return   panserv.calculerMontantTotalPanier(iduser);
     }
     @PostMapping(value = "/addpanier")
     public Panier addPanier(@RequestBody Panier pan) {
    	 return panserv.addpanier(pan);
     }
     @PostMapping(value = "/addlignepaniertopanier")
     public Panier addLignepanierToPanier(@RequestBody Ligne_panier lp) {
 		return panserv.addLignepanierToPanier(lp);
 	}
}
