package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entities.Commande;
import com.example.ecommerce.entities.Paiement;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.repository.CommandeRepository;
import com.example.ecommerce.repository.PaiementRepository;
import com.example.ecommerce.repository.PanierRepository;


@Service
public class CommandeService implements InterCommandeService{
    @Autowired
    CommandeRepository cmdrep;
	
    @Autowired
    PanierRepository panrep;
   
    @Autowired
    PaiementRepository payrep;
    
	@Override
	public Commande addCommande(Commande cmd,Long idpanier) {
		Panier pan =panrep.findById(idpanier).get();
		cmd.setPan(pan);
		return cmdrep.save(cmd);
	}
	
	@Override
	public String deleteCommande(Long id) {
		String ch="";
		cmdrep.deleteById(id);
		ch="Commande deleted successfuly !!!";
		return ch;
	}

	@Override
	public List<Commande> getAllCommande() {
		return cmdrep.findAll();
	}

	@Override
	public Commande updateCommande(Commande cmd, Long id) {
		Commande comm = cmdrep.findById(id).get();
		comm.setEtatCmd(cmd.getEtatCmd());
		return cmdrep.save(comm);
	}

	@Override
	public List<Commande> addListCommande(List<Commande> cmd) {
		
		return cmdrep.saveAll(cmd);
	}

	@Override
	public List<Commande> getAllCommandeByPanier(Long idpan) {
		return cmdrep.getAllByPanIdpanier(idpan);
	}

	@Override
	public Commande getCommandeById(Long idcmd) {
		return cmdrep.findById(idcmd).get();
	}

	@Override
	public Panier getPanierByCommande(Long idcmd) {
		Commande cmd = cmdrep.findById(idcmd).get();
		return cmd.getPan();
	}

	@Override
	public Commande addPaiementToCommande(Paiement pai, Long idcmd) {
		Commande cmd = cmdrep.findById(idcmd).get();
		Paiement pai1 = pai;
		pai.setCommande(cmd);
		pai = payrep.save(pai);
		
		cmd.setPaiement(pai1);
		return cmd = cmdrep.save(cmd);
	}


}
