package com.example.LibroDeRecetas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Receta {

    private Integer id;
    private String nombre;
    private List<Paso> pasos = new ArrayList<>();

    public Receta() {
    }

    public Receta(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Paso> getPasos() {
        return pasos;
    }

    public void setPasos(List<Paso> pasos) {
        this.pasos = pasos != null ? pasos : new ArrayList<>();
    }

    public void addPaso(Paso paso) {
        if (pasos == null) {
            pasos = new ArrayList<>();
        }
        pasos.add(paso);
    }
}
