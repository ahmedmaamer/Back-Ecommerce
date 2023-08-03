package com.example.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Utilisateur;


@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{
    Boolean existsByUsername(String Username);
    
    Optional<Utilisateur> findByUsername(String Username);

	Boolean existsByEmail(String email);
	
	public List<Utilisateur> getAllByAdresseIdAdr(Long AdresseIdAdr);
	
	@Query(value = "select * from Utilisateur u where u.Username like :cle%", nativeQuery = true )
	public List<Utilisateur> getUsersSWSC(@Param ("cle") String ch);
	
	@Query(value = "select uid from userrole ur where ur.idrole=1", nativeQuery = true )
	public List<Long> findIdByIdRole();
	
	@Query(value = "select uid from userrole ur where ur.idrole=?1", nativeQuery = true )
	public List<Long> findIduserByIdRole(Long idrole);
	
	@Query(value = "select * from Utilisateur u,userrole ur where u.uid=ur.uid and ur.idrole=?1", nativeQuery = true )
	public List<Utilisateur> findUsersByRole(Long idrole);
}
