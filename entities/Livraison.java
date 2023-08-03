package com.example.ecommerce.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
public class Livraison {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idL;
	
	@Enumerated(EnumType.STRING)
	private etatLivraison etatLiv;
	
	private float fraisLiv;
	private String delaisLiv;
	
	@OneToOne
	@JoinColumn(name = "paiement_id")
    private Paiement paiement;
	
}
