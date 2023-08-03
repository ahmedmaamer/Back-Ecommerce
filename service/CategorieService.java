package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ecommerce.entities.Categorie;
import com.example.ecommerce.repository.CategorieRepository;


@Service
public class CategorieService implements InterCategorieService{

	@Autowired
    CategorieRepository catrep;
	
	@Override
	public Categorie addCategorie(Categorie cat) {
		return catrep.save(cat);
	}

	@Override
	public void deleteCategorie(Long id) {
		catrep.deleteById(id);
	}
	
	@Override
	public List<Categorie> getAllCategorie() {
		return catrep.findAll();
	}
	@Override
	public Categorie getCategorieById(long id) {
		Optional<Categorie> cat = catrep.findById(id);
		return cat.get();
	}
	@Override
	public Categorie updateCategorie(Categorie cat, long id) {
		Categorie existingCategorie = catrep.findById(id)
				.orElseThrow(() -> new RuntimeException("Categorie not found"));

		existingCategorie.setCatlib(cat.getCatlib());

		catrep.save(existingCategorie);
		return existingCategorie;
	}

}
