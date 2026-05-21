package com.example.LibroDeRecetas.services;

import com.example.LibroDeRecetas.dto.CrearPasoRequest;
import com.example.LibroDeRecetas.model.Paso;
import com.example.LibroDeRecetas.storage.RecetaJsonStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PasoService {

    private final RecetaJsonStore recetaJsonStore;

    public PasoService(RecetaJsonStore recetaJsonStore) {
        this.recetaJsonStore = recetaJsonStore;
    }

    public Paso crearPaso(Integer recetaId, Paso paso) {
        try {
            return recetaJsonStore.addPaso(recetaId, paso);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el paso en JSON", e);
        }
    }

    public Paso crearPaso(CrearPasoRequest request) {
        Paso paso = new Paso(request.getDescripcion());
        return crearPaso(request.getRecetaId(), paso);
    }

    public List<Paso> listarPasosPorReceta(Integer recetaId) {
        if (!recetaJsonStore.existsById(recetaId)) {
            throw new RuntimeException("Receta no encontrada con ID: " + recetaId);
        }
        return recetaJsonStore.findPasosByRecetaId(recetaId);
    }

    public Paso actualizarPaso(Integer recetaId, Integer pasoId, Paso paso) {
        try {
            return recetaJsonStore.updatePaso(recetaId, pasoId, paso);
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar el paso en JSON", e);
        }
    }

    public void eliminarPaso(Integer recetaId, Integer pasoId) {
        try {
            recetaJsonStore.deletePaso(recetaId, pasoId);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el paso en JSON", e);
        }
    }
}
