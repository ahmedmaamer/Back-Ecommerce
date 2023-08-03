package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

}
