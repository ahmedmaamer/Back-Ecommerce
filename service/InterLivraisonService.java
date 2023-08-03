package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entities.Livraison;

public interface InterLivraisonService {
      public Livraison addLivraison(Livraison liv);
      
      public String deleteLivraison(Long id);
      
      public List<Livraison> getAllLivraison();
      
      public Livraison updateLivraison(Livraison liv,Long id);
}
