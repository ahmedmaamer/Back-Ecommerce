package com.example.ecommerce.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entities.Produit;
import com.example.ecommerce.service.ProduitService;
@CrossOrigin(origins="http://localhost:4200")
@RestController
public class ProduitController {
    @Autowired
    ProduitService prodserv;
    
    @PostMapping(value = "/addproduit/{idcategorie}")
    public Produit addProduit(@RequestBody Produit prod,@PathVariable Long idcategorie)
    {
    	return prodserv.addProduit(prod,idcategorie);
    }
    
    @PostMapping(value = "/addlistproduit")
    public List<Produit> addListProduit(@RequestBody List<Produit> prods)
    {
    	return prodserv.addListProduit(prods);
    }
    
    @GetMapping(value = "/getallprods")
    public List<Produit> getAllProds()
    {
    	return prodserv.getAllProds();
    }
    
    @DeleteMapping(value = "/deleteprod/{id}")
    public String deleteProd(@PathVariable long id)
    {
    	return prodserv.deleteProd(id);
    }
    
    @PutMapping(value = "/updateproduit/{id}")
    public Produit updateProduit(@RequestBody Produit prod,@PathVariable long id)
    {
    	return prodserv.updateProduit(prod, id);
    }
    
   /* @PostMapping(value = "/addproduitpanier/{idprod}/{idpanier}")
    public Produit addProduitPanier(@PathVariable Long idprod,@PathVariable Long idpanier)
    {
    	return prodserv.addProduitPanier(idprod,idpanier);
    }*/
    
    @GetMapping(value = "/getproduitbyid/{idprod}")
    public Produit getProduitByid(@PathVariable Long idprod) {
		
		return prodserv.getProduitByid(idprod);
	}
    
    @PostMapping(value = "/addproduitimage/{idprod}")
    public Produit addProduitImage(@PathVariable Long idprod,@RequestParam("file") MultipartFile file ) throws IOException
    {
    	return prodserv.addProduitImage(idprod,file);
    }
    
    @GetMapping(value = "/getallprodsbycatid/{catid}")
    public List<Produit> getAllProdsBycatid(@PathVariable Long catid) {
		
		return prodserv.getAllProdsBycatid(catid);
	}
    /*
    @GetMapping(value = "/getidprodfromidpanier")
    public List<Long> getIdprodFromIdpanier() {
		
		return prodserv.getIdprodFromIdpanier();
	}
	
	@GetMapping(value = "/getidpfromidpan/{idpan}")
    public List<Long> getIdpFromIdpan(@PathVariable Long idpan) {
		
		return prodserv.getIdpFromIdpan(idpan);
	}
	
	@GetMapping(value = "/getprodsfromidpanier/{idpan}")
    public List<Produit> getprodsFromIdpanier(@PathVariable Long idpan) {
		
		return prodserv.getprodsFromIdpanier(idpan);
	}*/
}
