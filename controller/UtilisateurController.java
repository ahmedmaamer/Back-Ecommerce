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

import com.example.ecommerce.entities.Adresse;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.entities.Utilisateur;
import com.example.ecommerce.service.UtilisateurService;
@CrossOrigin(origins="http://localhost:4200")
@RestController
public class UtilisateurController {

	@Autowired
	UtilisateurService userserv;
	
	@PostMapping(value = "/adduser/{idadress}")
    public Utilisateur adduser(@RequestBody Utilisateur user,@PathVariable Long idadress) {
		
		return userserv.addUser(user, idadress);
	}
	
	@PostMapping(value = "/addlistusers")
    public List<Utilisateur> addlistusers(@RequestBody List<Utilisateur> users) {
		
		return userserv.addListUsers(users);
	}
	
	@PostMapping(value = "/addUserWTUN")
    public String adduserWTUN(@RequestBody Utilisateur user) {
		
		return userserv.addUserWTUN(user);
	}
	
	@PutMapping(value = "/updateuser/{iduser}")
    public Utilisateur updateUser(@RequestBody Utilisateur user,@PathVariable long iduser) {
		
		return userserv.updateUser(user,iduser);
	}
	
	@GetMapping(value = "/getallusers")
    public List<Utilisateur> getAllUsers() {
		
		return userserv.getAllUsers();
	}
	
	@GetMapping(value = "/getuserBUN/{username}")
    public Utilisateur getuserBUN(@PathVariable String username) {
		
		return userserv.findByUsername(username);
	}
	
	@GetMapping(value = "/getlistusersSWSC/{ch}")
    public List<Utilisateur> getListUsersSWSC(@PathVariable String ch) {
		
		return userserv.getListUsersSWSC(ch);
	}
	
	@DeleteMapping(value = "/deleteuserBI/{iduser}")
	public String deleteuser(@PathVariable long iduser) {
		
		return userserv.deleteUser(iduser);
	}
	
	@PostMapping(value = "/adduserrole/{iduser}/{idrole}")
    public Utilisateur addUserRole(@PathVariable Long iduser,@PathVariable Long idrole) {
		
		return userserv.addUserRole(iduser, idrole);
	}
	
	@GetMapping(value = "/getlistidfromidrole")
    public List<Long> getListIdFromIdrole() {
		
		return userserv.getIdFromIdrole();
	}
	
	@GetMapping(value = "/getlistiduserfromidrole/{idrole}")
    public List<Long> getListIduserFromIdrole(@PathVariable Long idrole) {
		
		return userserv.getIduserFromIdrole(idrole);
	}
	
	@GetMapping(value = "/getlistusersfromidrole/{idrole}")
    public List<Utilisateur> getListUsersFromIdrole(@PathVariable Long idrole) {
		
		return userserv.getUsersFromIdrole(idrole);
	}
	
	@GetMapping(value ="/getallusersbyadress/{idadress}")
	public List<Utilisateur> getAllUsersByAdress(@PathVariable Long idadress) {
		return userserv.getAllUsersByAdress(idadress);
	}
	
	@GetMapping(value ="/getuserbyiduser/{iduser}")
	public Utilisateur getUserByIduser(@PathVariable Long iduser) {
		return userserv.getUserByIduser(iduser);
	}
	
	@GetMapping(value ="/getadressbyuser/{iduser}")
	public Adresse getadressByUser(@PathVariable Long iduser) {
		return userserv.getadressByUser(iduser);
	}
	
	@PostMapping(value = "/addpaniertouser/{iduser}")
	public Utilisateur addPanierToUser(@RequestBody Panier pan,@PathVariable Long iduser) {
		return userserv.addPanierToUser(pan, iduser);
	}
	@DeleteMapping(value = "/removePanierFromUser/{iduser}")
	public void removePanierFromUser(Long iduser) {
		userserv.removePanierFromUser(iduser);		
	}
	
	
	
}
