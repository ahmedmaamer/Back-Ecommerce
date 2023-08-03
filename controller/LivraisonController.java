package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.entities.Livraison;
import com.example.ecommerce.service.LivraisonService;
@CrossOrigin(origins="http://localhost:4200")
@RestController
public class LivraisonController {
   @Autowired
   LivraisonService livserv;
   
   @PostMapping(value = "/addlivraison")
   public Livraison addLivraison(@RequestBody Livraison liv)
   {
   	return livserv.addLivraison(liv);
   }
   
   @DeleteMapping(value = "/deletelivraison/{id}")
   public String deleteLivraison(@PathVariable Long id)
   {
   	return livserv.deleteLivraison(id);
   }
   
   @GetMapping(value = "/getalllivraison")
   public List<Livraison> getAllLivraison()
   {
   	return livserv.getAllLivraison();
   }
   
   @PostMapping(value = "/updatelivraison/{id}")
   public Livraison updateLivraison(@RequestBody Livraison liv,@PathVariable Long id)
   {
   	return livserv.updateLivraison(liv, id);
   }
}

