package com.example.ecommerce.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Paiement {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long IdP;
	
	@Enumerated(EnumType.STRING)
	private etatPaiement etatP;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:SSS")
	private Timestamp DateP;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "commande_id")
    private Commande commande;
	
	@OneToOne(mappedBy = "paiement")
    private Livraison livraison;
	
}
