package com.example.ecommerce.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class Ligne_panier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idlp;
	private Long qteAjoute;
	
	@ManyToOne
	@JoinColumn(name ="id_produit")
	private Produit prod;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="id_panier")
	private Panier panier;

	public Ligne_panier( Panier panier,Produit prod,Long qteAjoute) {
		super();
		this.qteAjoute = qteAjoute;
		this.prod = prod;
		this.panier = panier;
	}
	
	
}
