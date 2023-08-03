package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entities.Adresse;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.entities.Utilisateur;

public interface InterUtilisateurService {

	public Utilisateur addUser(Utilisateur user,Long idadress);
	
	public List<Utilisateur> addListUsers(List<Utilisateur> users);
	
	public String addUserWTUN(Utilisateur user);
	
	public Utilisateur updateUser(Utilisateur user, long iduser);
	
	public List<Utilisateur> getAllUsers();
	
	public String deleteUser(long id);
	
	public Utilisateur findByUsername(String username);
	
	public List<Utilisateur> getListUsersSWSC(String ch);
	
	public Utilisateur addUserRole(Long iduser, Long idrole);
	
	public List<Long> getIdFromIdrole();
	
	public List<Long> getIduserFromIdrole(Long idrole);
	
	public List<Utilisateur> getUsersFromIdrole(Long idrole);
	
    public List<Utilisateur> getAllUsersByAdress(Long idadress);
    
    public Utilisateur getUserByIduser(Long iduser);
    
    public Adresse getadressByUser(Long iduser);
    
    public Utilisateur addPanierToUser(Panier pan,Long iduser);
    
    public void removePanierFromUser(Long iduser);
}

