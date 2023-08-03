package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entities.Categorie;

public interface InterCategorieService {
	   public Categorie addCategorie(Categorie cat);
	   
	   public void deleteCategorie(Long id);
	   
	   public Categorie updateCategorie(Categorie cat, long id);
	   
	   public List<Categorie> getAllCategorie();
	    
	   public Categorie getCategorieById(long id);
}
