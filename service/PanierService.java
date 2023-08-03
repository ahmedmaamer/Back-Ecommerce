package com.example.ecommerce.service;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entities.Ligne_panier;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.entities.Utilisateur;
import com.example.ecommerce.repository.PanierRepository;
import com.example.ecommerce.repository.UtilisateurRepository;

@Service
public class PanierService implements InterPanierService{
 
	@Autowired
	PanierRepository panrep;
	
	@Autowired
	UtilisateurRepository userrep;
	@Autowired
	Ligne_panierService  lpserv;

	@Override
	public List<Panier> getAllPanier() {	
		return panrep.findAll();
	}

	@Transactional
	public void removeUserFromPanier(Long panierId) {
	    Optional<Panier> panierOptional = panrep.findById(panierId);
	    
	    if (panierOptional.isPresent()) {
	        Panier panier = panierOptional.get();
	        Utilisateur utilisateur = panier.getUser();

	        if (utilisateur != null) {
	            
	            panier.setUser(null);
	            utilisateur.setP(panier);
	            panrep.save(panier);
	            userrep.save(utilisateur);
	        } else {
	            throw new EntityNotFoundException("Panier does not have an associated user.");
	        }
	    } else {
	        throw new EntityNotFoundException("Panier not found.");
	    }
	}

		
	

	@Override
	public Panier getPanierById(Long idPan) {
		return panrep.findById(idPan).get();
	}

	@Override
	public Utilisateur getUserByPanier(Long idPan) {
		Panier pan = panrep.findById(idPan).get();
		return pan.getUser();
	}
	
	@Override
    public float calculerMontantTotalPanier(Long iduser) {
		Utilisateur utilisateur=userrep.findById(iduser).get();
        List<Ligne_panier> lignesPanier = lpserv.getLignesPanierUtilisateur(utilisateur);
        float montantTotal = 0;

        for (Ligne_panier lignePanier : lignesPanier) {
            montantTotal += lignePanier.getQteAjoute() * lignePanier.getProd().getProdPrix();
        }
        Panier pan = panrep.findByUserUid(iduser);
        pan.setMontantTot(montantTotal);
        panrep.save(pan);
        return montantTotal;
    }
	@Override
	public Panier getPanierByUser(Long iduser) {
		
		return panrep.findByUserUid(iduser);
	}

	@Override
	public Panier addpanier(Panier pan) {
		return panrep.save(pan);
	}

	@Override
	public Panier addLignepanierToPanier(Ligne_panier lp) {
		
		Panier pan = lp.getPanier();
		pan.addLignepanier(lp);
		return panrep.save(pan);
	}


}
