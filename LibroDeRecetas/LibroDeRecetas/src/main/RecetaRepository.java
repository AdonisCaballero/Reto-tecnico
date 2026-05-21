/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.LibroDeRecetas.repository;

import com.example.LibroDeRecetas.model.Paso;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author adoni
 */
public interface PasoRepository extends JpaRepository<Paso,Integer> {
    
}
