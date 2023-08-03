package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entities.Livraison;
import com.example.ecommerce.entities.Paiement;
import com.example.ecommerce.repository.CommandeRepository;
import com.example.ecommerce.repository.LivraisonRepository;
import com.example.ecommerce.repository.PaiementRepository;

@Service
public class PaiementService implements InterPaiementService{
    @Autowired
	PaiementRepository payrep;

    @Autowired
    CommandeRepository cmdrep;
    
    @Autowired
    LivraisonRepository livrep;
    
	@Override
	public String deletePaiement(Long id) {
		String ch="";
		payrep.deleteById(id);
		ch="Paiement deleted successfuly !!!";
		return ch;
	}

	@Override
	public List<Paiement> getAllPaiement() {
		return payrep.findAll();
	}

	@Override
	public Paiement updatePaiement(Paiement pay, Long id) {
		Paiement p = payrep.findById(id).get();
		p.setEtatP(pay.getEtatP());
		return payrep.save(p);
	}

	@Override
	public Paiement addPaiement(Paiement pai) {
		
		return payrep.save(pai);
	}

	@Override
	public Paiement addLivraisonToPaiement(Livraison liv, Long idpai) {
		Paiement pai = payrep.findById(idpai).get();
		Livraison liv1 = liv;
		liv.setPaiement(pai);
		liv = livrep.save(liv);
		
		pai.setLivraison(liv1);
		return payrep.save(pai);
	}

}
