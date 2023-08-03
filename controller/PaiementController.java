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

import com.example.ecommerce.entities.Livraison;
import com.example.ecommerce.entities.Paiement;
import com.example.ecommerce.service.PaiementService;


@CrossOrigin(origins="http://localhost:4200")
@RestController
public class PaiementController {

	   @Autowired
	   PaiementService payserv;
	   
	   @PostMapping(value = "/addpaiement")
	   public Paiement addPaiement(@RequestBody Paiement pai) {	
			return payserv.addPaiement(pai);
		}
	
	   @DeleteMapping(value = "/deletepaiement/{id}")
	   public String deletePaiement(@PathVariable Long id)
	   {
	   	return payserv.deletePaiement(id);
	   }
	   
	   @GetMapping(value = "/getallpaiement")
	   public List<Paiement> getAllPaiement()
	   {
	   	return payserv.getAllPaiement();
	   }
	   
	   @PutMapping(value = "/updatepaiement/{id}")
	   public Paiement updatePaiement(@RequestBody Paiement pay,@PathVariable Long id)
	   {
	   	return payserv.updatePaiement(pay, id);
	   }
	   
	   @PostMapping(value = "/addlivraisontopaiement/{idpai}")
	   public Paiement addLivraisonToPaiement(@RequestBody Livraison liv,@PathVariable Long idpai) {
		   return payserv.addLivraisonToPaiement(liv, idpai);
	   }
}
