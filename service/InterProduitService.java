package com.example.ecommerce.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entities.Produit;

public interface InterProduitService {

   public Produit addProduit(Produit prod,Long idcategorie);
   
   public List<Produit> addListProduit(List<Produit> prods);
   
   public List<Produit> getAllProds();
   
   public List<Produit> getAllProdsBycatid(Long catid);
   
   public String deleteProd(long id);
   
   public Produit updateProduit(Produit prod, long id);
   
   public Produit addProduitImage(Long idprod ,MultipartFile file) throws IOException;
   
   public Produit getProduitByid(Long idprod);
   
 /*  public Produit addProduitPanier(Long idprod,Long idpanier);
	
   public List<Long> getIdprodFromIdpanier();
	
   public List<Long> getIdpFromIdpan(Long idpan);
	
   public List<Produit> getprodsFromIdpanier(Long idpan);*/
}
