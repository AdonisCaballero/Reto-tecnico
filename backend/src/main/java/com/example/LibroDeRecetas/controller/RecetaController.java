package com.example.LibroDeRecetas.controller;

import com.example.LibroDeRecetas.model.Paso;
import com.example.LibroDeRecetas.model.Receta;
import com.example.LibroDeRecetas.services.PasoService;
import com.example.LibroDeRecetas.services.RecetaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/recetas")
@CrossOrigin(origins = "http://localhost:4200")
public class RecetaController {

    private final RecetaService recetaService;
    private final PasoService pasoService;

    public RecetaController(RecetaService recetaService, PasoService pasoService) {
        this.recetaService = recetaService;
        this.pasoService = pasoService;
    }

    @GetMapping
    public List<Receta> obtenerRecetas() {
        return recetaService.listarRecetas();
    }

    @GetMapping("/{id}")
    public Receta obtenerReceta(@PathVariable Integer id) {
        try {
            return recetaService.obtenerPorId(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Receta crearReceta(@RequestBody Receta receta) {
        return recetaService.crearReceta(receta);
    }

    @PutMapping("/{id}")
    public Receta actualizarReceta(@PathVariable Integer id, @RequestBody Receta receta) {
        try {
            return recetaService.actualizarReceta(id, receta);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarReceta(@PathVariable Integer id) {
        try {
            recetaService.eliminarReceta(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/pasos")
    public List<Paso> obtenerPasos(@PathVariable Integer id) {
        try {
            return pasoService.listarPasosPorReceta(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{id}/pasos")
    public Paso agregarPaso(@PathVariable Integer id, @RequestBody Paso paso) {
        try {
            return pasoService.crearPaso(id, paso);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("no encontrada")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping("/{recetaId}/pasos/{pasoId}")
    public Paso actualizarPaso(
            @PathVariable Integer recetaId,
            @PathVariable Integer pasoId,
            @RequestBody Paso paso) {
        try {
            return pasoService.actualizarPaso(recetaId, pasoId, paso);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{recetaId}/pasos/{pasoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPaso(@PathVariable Integer recetaId, @PathVariable Integer pasoId) {
        try {
            pasoService.eliminarPaso(recetaId, pasoId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
