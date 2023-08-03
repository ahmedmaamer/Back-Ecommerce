package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.entities.Categorie;
import com.example.ecommerce.service.CategorieService;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class CategorieController {
    
	@Autowired
	CategorieService catserv;
	
	@PostMapping(value = "/addcategorie")
    public Categorie addCategorie(@RequestBody Categorie cat)
    {
    	return catserv.addCategorie(cat);
    }
    
    @DeleteMapping(value = "/deletecategorie/{id}")
    public void deleteCategorie(@PathVariable Long id)
    {
      catserv.deleteCategorie(id);	
    }
    
    @GetMapping(value = "/getcategorie")
    public List<Categorie> getAllCategorie(){
        return catserv.getAllCategorie();
    }
    //build get Categorie by id
    @GetMapping(value = "/getcategorie/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable("id") long catId){
        return new ResponseEntity<Categorie>(catserv.getCategorieById(catId), HttpStatus.OK);

    }
    //build update categorie REST API
    @PutMapping(value = "/updatecategorie/{id}")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable("id") long catId,@RequestBody Categorie cat){

        return  new  ResponseEntity<Categorie>(catserv.updateCategorie(cat, catId),HttpStatus.OK);

    }
}
