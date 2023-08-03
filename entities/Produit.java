package com.example.ecommerce.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Produit{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long prodref;
	private float prodPrix;
	private String prodMarque;
	private String prodDescription;
	private long prodQte;
	@Enumerated(EnumType.STRING)
	private ProdDisponibilite prodDispo;
	
	@Enumerated(EnumType.STRING)
	private ProduitTaille taille;
	private String couleur;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private Categorie categorie;
	
	@JsonIgnore
	@OneToMany(mappedBy= "prod")
	private List<Ligne_panier> listpaniers = new ArrayList<>();
    
	
	@OneToOne
	private Image img;
	
	/*@JsonIgnore
	@ManyToMany
	@JoinTable(
			name ="produitpanier",
			joinColumns = @JoinColumn(name ="prodref"),
			inverseJoinColumns = @JoinColumn(name ="Idpanier")
	)
    private Set<Panier> listpaniers= new HashSet<>();*/
	
	
	/*public void addPanier(Panier panier)
	{
		this.listpaniers.add(panier);
	}*/
	
}
