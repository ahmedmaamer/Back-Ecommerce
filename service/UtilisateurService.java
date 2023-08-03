package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entities.Adresse;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.entities.Role;
import com.example.ecommerce.entities.Utilisateur;
import com.example.ecommerce.repository.AdresseRepository;
import com.example.ecommerce.repository.PanierRepository;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UtilisateurRepository;

@Service
public class UtilisateurService implements InterUtilisateurService {

	@Autowired
	UtilisateurRepository userrep;
 
	@Autowired
	RoleRepository rolerep;
	
	@Autowired
	AdresseRepository adressrep;
	
	@Autowired
	PanierRepository Panrep;
	
	@Autowired
	PanierService panserv;
	@Override
	public Utilisateur addUser(Utilisateur user,Long idadress) {
		Adresse adr = adressrep.findById(idadress).get();
		user.setAdresse(adr);
	    Panier pan = panserv.addpanier(user.getP());
	    user.setP(pan);
		return userrep.save(user);
	}

	@Override
	public String deleteUser(long id) {
		String ch="";
		userrep.deleteById(id);
		ch = "user deleted successfuly !!!";
		return ch;
	}

	@Override
	public List<Utilisateur> addListUsers(List<Utilisateur> users) {
		
		return userrep.saveAll(users);
	}

	@Override
	public String addUserWTUN(Utilisateur user) {
		String ch="";
		if(userrep.existsByUsername(user.getUsername())) {
			ch="username already exist !!!";
		}
		else{
			userrep.save(user);
			ch="user added successfuly !!!";
		}
		
		return ch;
	}
    
	@Override
	public Utilisateur updateUser(Utilisateur user, long iduser) {
		
		Utilisateur usr = userrep.findById(iduser).get();
		usr.setPassword(user.getPassword());
		usr.setUtel(user.getUtel());
		return userrep.save(usr);
	}
    
	@Override
	public List<Utilisateur> getAllUsers() {
		
		return userrep.findAll();
	}

	@Override
	public List<Utilisateur> getListUsersSWSC(String ch) {
		
		return userrep.getUsersSWSC(ch);
	}

	@Override
	public Utilisateur findByUsername(String username) {
		
		return userrep.findByUsername(username).get();
	}

	@Override
	public Utilisateur addUserRole(Long iduser, Long idrole) {
		Utilisateur usr = userrep.findById(iduser).get();
		Role role = rolerep.findById(idrole).get();
		usr.addRole(role);
		return userrep.save(usr);
	}

	@Override
	public List<Long> getIdFromIdrole() {
		return userrep.findIdByIdRole();
	}

	@Override
	public List<Long> getIduserFromIdrole(Long idrole) {
		return userrep.findIduserByIdRole(idrole);
	}

	@Override
	public List<Utilisateur> getUsersFromIdrole(Long idrole) {
		return userrep.findUsersByRole(idrole);
	}

	@Override
	public List<Utilisateur> getAllUsersByAdress(Long idadress) {
		return userrep.getAllByAdresseIdAdr(idadress);
	}

	@Override
	public Utilisateur getUserByIduser(Long iduser) {
		return userrep.findById(iduser).get();
	}

	@Override
	public Adresse getadressByUser(Long iduser) {
		Utilisateur usr = userrep.findById(iduser).get();
		return usr.getAdresse();
	}

	@Override
	public Utilisateur addPanierToUser(Panier pan, Long iduser) {
		Utilisateur user = userrep.findById(iduser).get();
		Panier pan1=pan;
		pan.setUser(user);
		pan = Panrep.save(pan);
		
		user.setP(pan1);
		return userrep.save(user);
	}

	@Transactional
    public void removePanierFromUser(Long userId) {
        Optional<Utilisateur> utilisateurOptional = userrep.findById(userId);
        
        if (utilisateurOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            Panier panier = utilisateur.getP();

                if (panier != null) {
                    utilisateur.setP(panier);
                    panier.setUser(null);
                   /* for (Commande commande : panier.getListCommandes()) {
                        commandeRepository.delete(commande);
                    }*/

                    Panrep.delete(panier);
                } else {
                    throw new EntityNotFoundException("User does not have a panier.");
                }
            } else {
                throw new EntityNotFoundException("User not found.");
            }
    }
	
	
}
