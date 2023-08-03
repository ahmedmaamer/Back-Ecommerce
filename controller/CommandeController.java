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

import com.example.ecommerce.entities.Commande;
import com.example.ecommerce.entities.Paiement;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.service.CommandeService;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class CommandeController {
      
    @Autowired
    CommandeService cmdserv;
    
    @PostMapping(value = "/addcommande/{idpanier}")
    public Commande addCommande(@RequestBody Commande cmd,@PathVariable Long idpanier)
    {
    	return cmdserv.addCommande(cmd,idpanier);
    }

    @DeleteMapping(value = "/deletecommande/{id}")
    public String deleteCommande(@PathVariable Long id)
    {
    	return cmdserv.deleteCommande(id);
    }
    
    @GetMapping(value = "/getallcommande")
    public List<Commande> getAllCommande()
    {
    	return cmdserv.getAllCommande();
    }
    
    @PostMapping(value = "/updatecommande/{id}")
    public Commande updateCommande(@RequestBody Commande cmd,@PathVariable Long id)
    {
    	return cmdserv.updateCommande(cmd, id);
    }
    
    @PostMapping(value = "/addlistcommande")
    public List<Commande> addListCommande(@RequestBody List<Commande> cmd)
    {
    	return cmdserv.addListCommande(cmd);
    }
    @GetMapping(value = "/getallcommandebypanier/{idpan}")
    public List<Commande> getAllCommandeByPanier(@PathVariable Long idpan) {
		
		return cmdserv.getAllCommandeByPanier(idpan);
	 }
    @GetMapping(value = "/getcommandebyid/{idcmd}")
    public Commande getCommandeById(@PathVariable Long idcmd) {
		
		return cmdserv.getCommandeById(idcmd);
	 }
    @GetMapping(value = "/getpanierbycommande/{idcmd}")
    public Panier getPanierByCommande(@PathVariable Long idcmd) {
   	 return cmdserv.getPanierByCommande(idcmd);
	}
    
    @PostMapping(value = "/addpaiementtocommande/{idcmd}")
    public Commande addPaiementToCommande(@RequestBody Paiement pai, @PathVariable Long idcmd) {
        return cmdserv.addPaiementToCommande(pai, idcmd);    
    }
    
}
