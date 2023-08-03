package com.example.ecommerce.service;

import com.example.ecommerce.entities.Adresse;

public interface InterAdresseService {
   public Adresse addAdressToUser(Long iduser, Adresse adr);
   
   public String deleteAdress(Long id);
}
