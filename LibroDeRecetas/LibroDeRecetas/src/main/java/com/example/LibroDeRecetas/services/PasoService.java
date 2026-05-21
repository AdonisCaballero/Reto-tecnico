package com.example.LibroDeRecetas.services;

import com.example.LibroDeRecetas.model.Paso;
import com.example.LibroDeRecetas.model.Receta;
import com.example.LibroDeRecetas.repository.PasoRepository;
import com.example.LibroDeRecetas.repository.RecetaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PasoService {

    private final PasoRepository pasoRepository;
    private final RecetaRepository recetaRepository;

    public PasoService(PasoRepository pasoRepository, RecetaRepository recetaRepository) {
        this.pasoRepository = pasoRepository;
        this.recetaRepository = recetaRepository;
    }

    public Paso crearPaso(Integer recetaId, Paso paso) {
        Receta receta = recetaRepository.findById(recetaId)
            .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + recetaId));
        
        paso.setReceta(receta);
        return pasoRepository.save(paso);
    }

    public List<Paso> listarPasos() {
        return pasoRepository.findAll();
    }
}
