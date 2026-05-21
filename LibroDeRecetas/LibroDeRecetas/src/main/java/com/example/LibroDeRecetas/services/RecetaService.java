package com.example.LibroDeRecetas.services;


import com.example.LibroDeRecetas.repository.recetas.model.Receta;
import com.example.LibroDeRecetas.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecetaService {

        private final RecetaRepository recetaRepository;

        public RecetaService(RecetaRepository recetaRepository) {
            this.recetaRepository = recetaRepository;
        }

        public Receta crearReceta(Receta receta) {
            return recetaRepository.save(receta);
        }

        public List listarRecetas() {
            return recetaRepository.findAll();
    }
}
