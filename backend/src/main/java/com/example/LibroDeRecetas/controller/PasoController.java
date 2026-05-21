package com.example.LibroDeRecetas.controller;

import com.example.LibroDeRecetas.dto.CrearPasoRequest;
import com.example.LibroDeRecetas.model.Paso;
import com.example.LibroDeRecetas.services.PasoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pasos")
@CrossOrigin(origins = "http://localhost:4200")
public class PasoController {

    private final PasoService pasoService;

    public PasoController(PasoService pasoService) {
        this.pasoService = pasoService;
    }

    @PostMapping
    public Paso crearPaso(@RequestBody CrearPasoRequest request) {
        try {
            return pasoService.crearPaso(request);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
