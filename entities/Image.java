package com.example.ecommerce.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Image implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImage;
    private String imageLib;
    private String fileType;
    
    @Column(length = 420000)
    private byte[] tailleFile;
    
    @JsonIgnore
    @OneToOne(mappedBy = "img")
    private Produit prod;

	public Image(String imageLib, String fileType, byte[] tailleFile) {
		super();
		this.imageLib = imageLib;
		this.fileType = fileType;
		this.tailleFile = tailleFile;
	}
    
    
}


