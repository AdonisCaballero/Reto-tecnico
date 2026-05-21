package com.example.LibroDeRecetas.services;

import com.example.LibroDeRecetas.model.Receta;
import com.example.LibroDeRecetas.storage.RecetaJsonStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RecetaService {

    private final RecetaJsonStore recetaJsonStore;

    public RecetaService(RecetaJsonStore recetaJsonStore) {
        this.recetaJsonStore = recetaJsonStore;
    }

    public List<Receta> listarRecetas() {
        return recetaJsonStore.findAll();
    }

    public Receta obtenerPorId(Integer id) {
        return recetaJsonStore.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
    }

    public Receta crearReceta(Receta receta) {
        try {
            receta.setId(null);
            return recetaJsonStore.save(receta);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la receta en JSON", e);
        }
    }

    public Receta actualizarReceta(Integer id, Receta datos) {
        try {
            Receta receta = obtenerPorId(id);
            receta.setNombre(datos.getNombre());
            return recetaJsonStore.save(receta);
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar la receta en JSON", e);
        }
    }

    public void eliminarReceta(Integer id) {
        try {
            recetaJsonStore.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la receta en JSON", e);
        }
    }
}
