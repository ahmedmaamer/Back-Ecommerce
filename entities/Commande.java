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

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Commande {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long CmdId;
	@CreationTimestamp
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:SSS")
	private Timestamp CmdDate;
	@Enumerated(EnumType.STRING)
	private etatCommande etatCmd;
	
	@OneToOne
	@JoinColumn(name = "panier_id")
	private Panier pan;
	
	@OneToOne(mappedBy = "commande")
    private Paiement paiement;
	
}
