package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.entities.Adresse;
import com.example.ecommerce.service.AdresseService;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class AdresseController {
    @Autowired
    AdresseService adresseserv;
    
    @PostMapping(value = "/addAdressToUser/{iduser}")
    public Adresse addAdressToUser(@PathVariable Long iduser, @RequestBody Adresse adr)
    {
    	return adresseserv.addAdressToUser(iduser,adr);
    	
    }
    
    @DeleteMapping(value = "/deleteadress/{id}")
    public String deleteAdress(@PathVariable Long id)
    {
      return adresseserv.deleteAdress(id);	
    }
}
