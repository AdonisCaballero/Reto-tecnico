package com.example.LibroDeRecetas.model;

import java.util.ArrayList;
import java.util.List;

public class Receta {

    private int id;
    private String nombre;
    private List<paso> pasos = new ArrayList<>();

    public Receta() {
    }

    public Receta(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<paso> getPasos() {
        return pasos;
    }

    public void setPasos(List<paso> pasos) {
        this.pasos = pasos;
    }
}