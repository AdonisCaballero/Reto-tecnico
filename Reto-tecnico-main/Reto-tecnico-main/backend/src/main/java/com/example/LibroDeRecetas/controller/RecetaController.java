package com.example.LibroDeRecetas.controller;


import com.example.LibroDeRecetas.model.Receta;
import com.example.LibroDeRecetas.model.paso;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    private List<Receta> recetas = new ArrayList<>();

    // GET /recetas
    @GetMapping
    public List<Receta> obtenerRecetas() {
        return recetas;
    }

    // POST /recetas
    @PostMapping
    public Receta crearReceta(@RequestBody Receta receta) {
        recetas.add(receta);
        return receta;
    }

    // GET /recetas/{id}/pasos
    @GetMapping("/{id}/pasos")
    public List<paso> obtenerPasos(@PathVariable int id) {

        for (Receta receta : recetas) {
            if (receta.getId() == id) {
                return receta.getPasos();
            }
        }

        return new ArrayList<>();
    }

    // POST /recetas/{id}/pasos
    @PostMapping("/{id}/pasos")
    public paso agregarPaso(
            @PathVariable int id,
            @RequestBody paso paso) {

        for (Receta receta : recetas) {
            if (receta.getId() == id) {

                receta.getPasos().add(paso);

                return paso;
            }
        }

        return null;
    }
}