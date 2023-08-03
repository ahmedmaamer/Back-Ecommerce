package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Adresse;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {

}
