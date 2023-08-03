package com.example.ecommerce.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Categorie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long catid;
	private String catlib;
	
	@OneToMany(mappedBy = "categorie")
	private List<Produit> listproduits;
}
