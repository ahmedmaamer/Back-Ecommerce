package com.example.ecommerce.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class Panier {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idpanier;
	private float montantTot;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private Utilisateur user;
	
	@JsonIgnore
	@OneToOne(mappedBy = "pan")
	private Commande cmd;
	
	
	@OneToMany(mappedBy= "panier")
	private List<Ligne_panier> listprods = new ArrayList<>();
	
	public void addLignepanier(Ligne_panier lp){
    	this.listprods.add(lp) ;
    }
	
	/*@ManyToMany(mappedBy = "listpaniers")
    private Set<Produit> listproduits= new HashSet<>();*/
}
