package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long>{
 
	@Query(value = "select prodref from produitpanier pp where pp.Idpanier=1", nativeQuery = true )
	public List<Long> findIdproduitByIdpanier();
	
	@Query(value = "select prodref from produitpanier pp where pp.Idpanier=?1", nativeQuery = true )
	public List<Long> findIdprodByIdpan(Long Idpanier);
	
	@Query(value = "select * from Produit p,produitpanier pp where p.prodRef=pp.prodRef and pp.Idpanier=?1", nativeQuery = true )
	public List<Produit> findProdsByPanier(Long Idpanier);
	
	public List<Produit> findByCategorie_catid(Long CategoryId);
}
