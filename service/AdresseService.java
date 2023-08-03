package com.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entities.Adresse;
import com.example.ecommerce.entities.Utilisateur;
import com.example.ecommerce.repository.AdresseRepository;
import com.example.ecommerce.repository.UtilisateurRepository;

@Service
public class AdresseService implements InterAdresseService{
    @Autowired
    AdresseRepository adresserep;
	
    @Autowired
    UtilisateurRepository userrep;
    
	@Override
	public Adresse addAdressToUser(Long iduser, Adresse adr) {
		Utilisateur us=userrep.findById(iduser).get();
    	us.setAdresse(adr);
		return adresserep.save(adr);
	}

	@Override
	public String deleteAdress(Long id) {
		
		String ch="";
		adresserep.deleteById(id);
		ch="adress deleted successfuly !!!";
		return ch;
	}
     
	
}
