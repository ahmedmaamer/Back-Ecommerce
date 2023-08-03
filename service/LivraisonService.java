package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entities.Livraison;
import com.example.ecommerce.repository.LivraisonRepository;

@Service
public class LivraisonService implements InterLivraisonService{
    @Autowired
    LivraisonRepository livrep;
	
	@Override
	public Livraison addLivraison(Livraison liv) {
		return livrep.save(liv);
	}

	@Override
	public String deleteLivraison(Long id) {
		String ch="";
		livrep.deleteById(id);
		ch="livraison deleted successfuly !!!";
		return ch;
	}

	@Override
	public List<Livraison> getAllLivraison() {
		return livrep.findAll();
	}

	@Override
	public Livraison updateLivraison(Livraison liv, Long id) {
		Livraison livr = livrep.findById(id).get();
		livr.setEtatLiv(liv.getEtatLiv());
		livr.setDelaisLiv(liv.getDelaisLiv());
		return livrep.save(livr);
	}
	

}
