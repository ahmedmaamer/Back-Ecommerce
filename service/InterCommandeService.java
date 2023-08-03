package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entities.Commande;
import com.example.ecommerce.entities.Paiement;
import com.example.ecommerce.entities.Panier;

public interface InterCommandeService {
         public Commande addCommande(Commande cmd,Long idpanier);
         
         public List<Commande> addListCommande(List<Commande> cmd);
         
         public List<Commande> getAllCommande();
         
         public String deleteCommande(Long idcmd);
         
         public Commande updateCommande(Commande cmd, Long idcmd);
         
         public List<Commande> getAllCommandeByPanier(Long idpan);
         
         public Commande getCommandeById(Long idcmd);
         
         public Panier getPanierByCommande(Long idcmd);
         
         public Commande addPaiementToCommande(Paiement pai,Long idcmd);

         
}
