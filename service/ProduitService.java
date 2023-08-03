package com.example.ecommerce.service;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entities.Categorie;
import com.example.ecommerce.entities.Image;
import com.example.ecommerce.entities.Produit;
import com.example.ecommerce.repository.CategorieRepository;
import com.example.ecommerce.repository.PanierRepository;
import com.example.ecommerce.repository.ProduitRepository;

@Service
public class ProduitService implements InterProduitService {
    
	@Autowired
	ProduitRepository prodrep;
	
	@Autowired
	CategorieRepository catrep;
	
	@Autowired
	PanierRepository panrep;
	
	@Autowired
	ImageService imageserv;
	@Override
	public Produit addProduit(Produit prod,Long idcategorie) {
		Categorie cat = catrep.findById(idcategorie).get();
		prod.setCategorie(cat);
		return prodrep.save(prod);
	}

	@Override
	public List<Produit> addListProduit(List<Produit> prods) {
		
		return prodrep.saveAll(prods);
	}

	@Override
	public List<Produit> getAllProds() {
		
		return prodrep.findAll();
	}

	@Override
	public String deleteProd(long id) {
		String ch="";
		prodrep.deleteById(id);
		ch = "product deleted successfuly !!!";
		return ch;
	}

	@Override
	public Produit updateProduit(Produit prod, long id) {
		Produit produit = prodrep.findById(id).get();
		produit.setProdPrix(prod.getProdPrix());
		produit.setProdQte(prod.getProdQte());
		produit.setCouleur(prod.getCouleur());
		produit.setProdDispo(prod.getProdDispo());
		produit.setProdMarque(prod.getProdMarque());
		produit.setProdDescription(prod.getProdDescription());
		produit.setImg(prod.getImg());
		produit.setTaille(prod.getTaille());
		return prodrep.save(produit);
	}


	@Override
	public Produit addProduitImage(Long idprod, MultipartFile file) throws IOException {
		Produit prod = prodrep.findById(idprod).get();
		
		Image img = imageserv.addImage(file);
		prod.setImg(img);
		return prodrep.save(prod);
	}

	@Override
	public Produit getProduitByid(Long idprod) {
		
		return prodrep.findById(idprod).get();
	}

	@Override
	public List<Produit> getAllProdsBycatid(Long catid) {
		
		return prodrep.findByCategorie_catid(catid);
	}

	/*@Override
	public List<Long> getIdprodFromIdpanier() {
		return prodrep.findIdproduitByIdpanier();
	}

	@Override
	public List<Long> getIdpFromIdpan(Long idpan) {
		return prodrep.findIdprodByIdpan(idpan);
	}

	@Override
	public List<Produit> getprodsFromIdpanier(Long idpan) {
		return prodrep.findProdsByPanier(idpan);
	}
    */
	

	/*@Override
	public Produit addProduitPanier(Long idprod, Long idpanier) {
		Panier pan = panrep.findById(idpanier).get();
		Produit prod = prodrep.findById(idprod).get();
		prod.addPanier(pan);
		return prodrep.save(prod);
	}*/
}
