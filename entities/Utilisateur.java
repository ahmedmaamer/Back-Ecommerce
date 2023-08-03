package com.example.ecommerce.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

@Table(	name = "Utilisateur", 
uniqueConstraints = { 
	@UniqueConstraint(columnNames = "username"),
	@UniqueConstraint(columnNames = "email") 
})
public class Utilisateur {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
     private long uid;
	
	@Column(nullable = false, unique = true)
	 private String username;
	 private String unom;
     private String uprenom;
     @Email
     private String email;
     private long utel;
     @NotBlank
     private String password;
     
     @JsonIgnore
 	@ManyToMany
 	@JoinTable(
 			name ="userrole",
 			joinColumns = @JoinColumn(name ="uid"),
 			inverseJoinColumns = @JoinColumn(name ="idrole")
 	)
     private Set<Role> listroles= new HashSet<>();
    
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Panier p;	
    
    @ManyToOne
    private Adresse adresse;
    
    

    public void addRole(Role role){
    	this.listroles.add(role) ;
    }



	public Utilisateur(String username, String unom, String uprenom, @Email String email, long utel,
			@NotBlank String password) {
		super();
		this.username = username;
		this.unom = unom;
		this.uprenom = uprenom;
		this.email = email;
		this.utel = utel;
		this.password = password;
	}
} 
	
     
     
     

