package com.example.LibroDeRecetas.storage;

import com.example.LibroDeRecetas.model.Paso;
import com.example.LibroDeRecetas.model.Receta;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RecetaJsonStore {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path dataFile;
    private final List<Receta> recetas = new ArrayList<>();
    private final Object lock = new Object();

    public RecetaJsonStore(@Value("${app.data.file:./data/recetas.json}") String filePath) {
        this.dataFile = Path.of(filePath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() throws IOException {
        synchronized (lock) {
            Files.createDirectories(dataFile.getParent());
            if (Files.exists(dataFile)) {
                loadFromDisk();
            } else {
                recetas.clear();
                persistToDisk();
            }
        }
    }

    public List<Receta> findAll() {
        synchronized (lock) {
            return deepCopy(recetas);
        }
    }

    public Optional<Receta> findById(Integer id) {
        synchronized (lock) {
            return recetas.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst()
                    .map(this::copyReceta);
        }
    }

    public boolean existsById(Integer id) {
        synchronized (lock) {
            return recetas.stream().anyMatch(r -> r.getId().equals(id));
        }
    }

    public Receta save(Receta receta) throws IOException {
        synchronized (lock) {
            if (receta.getId() == null) {
                receta.setId(nextRecetaId());
                if (receta.getPasos() == null) {
                    receta.setPasos(new ArrayList<>());
                } else {
                    for (Paso paso : receta.getPasos()) {
                        paso.setId(nextPasoId());
                    }
                }
                recetas.add(copyReceta(receta));
            } else {
                Receta existente = recetas.stream()
                        .filter(r -> r.getId().equals(receta.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + receta.getId()));
                existente.setNombre(receta.getNombre());
                if (receta.getPasos() != null) {
                    existente.setPasos(mutablePasosCopy(receta.getPasos()));
                }
            }
            persistToDisk();
            return copyReceta(recetas.stream()
                    .filter(r -> r.getId().equals(receta.getId()))
                    .findFirst()
                    .orElse(receta));
        }
    }

    public Paso addPaso(Integer recetaId, Paso paso) throws IOException {
        synchronized (lock) {
            Receta receta = recetas.stream()
                    .filter(r -> r.getId().equals(recetaId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + recetaId));

            paso.setId(nextPasoId());
            receta.addPaso(copyPaso(paso));
            persistToDisk();

            return receta.getPasos().get(receta.getPasos().size() - 1);
        }
    }

    public List<Paso> findPasosByRecetaId(Integer recetaId) {
        synchronized (lock) {
            return findById(recetaId)
                    .map(Receta::getPasos)
                    .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + recetaId));
        }
    }

    public Paso updatePaso(Integer recetaId, Integer pasoId, Paso datos) throws IOException {
        synchronized (lock) {
            Receta receta = recetas.stream()
                    .filter(r -> r.getId().equals(recetaId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + recetaId));

            Paso paso = receta.getPasos().stream()
                    .filter(p -> p.getId().equals(pasoId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Paso no encontrado con ID: " + pasoId));

            paso.setDescripcion(datos.getDescripcion());
            persistToDisk();
            return copyPaso(paso);
        }
    }

    public void deletePaso(Integer recetaId, Integer pasoId) throws IOException {
        synchronized (lock) {
            Receta receta = recetas.stream()
                    .filter(r -> r.getId().equals(recetaId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + recetaId));

            boolean removed = receta.getPasos().removeIf(p -> p.getId().equals(pasoId));
            if (!removed) {
                throw new RuntimeException("Paso no encontrado con ID: " + pasoId);
            }
            persistToDisk();
        }
    }

    public void deleteById(Integer id) throws IOException {
        synchronized (lock) {
            boolean removed = recetas.removeIf(r -> r.getId().equals(id));
            if (!removed) {
                throw new RuntimeException("Receta no encontrada con ID: " + id);
            }
            persistToDisk();
        }
    }

    private void loadFromDisk() throws IOException {
        List<Receta> loaded = objectMapper.readValue(
                dataFile.toFile(),
                new TypeReference<List<Receta>>() {}
        );
        recetas.clear();
        if (loaded != null) {
            loaded.forEach(r -> {
                if (r.getPasos() == null) {
                    r.setPasos(new ArrayList<>());
                }
                r.setPasos(mutablePasosCopy(r.getPasos()));
                recetas.add(r);
            });
        }
    }

    private void persistToDisk() throws IOException {
        byte[] json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(recetas);
        Files.write(dataFile, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private int nextRecetaId() {
        return recetas.stream()
                .mapToInt(Receta::getId)
                .max()
                .orElse(0) + 1;
    }

    private int nextPasoId() {
        return recetas.stream()
                .flatMap(r -> r.getPasos().stream())
                .mapToInt(Paso::getId)
                .max()
                .orElse(0) + 1;
    }

    private List<Receta> deepCopy(List<Receta> source) {
        return source.stream().map(this::copyReceta).toList();
    }

    private Receta copyReceta(Receta original) {
        Receta copia = new Receta();
        copia.setId(original.getId());
        copia.setNombre(original.getNombre());
        copia.setPasos(mutablePasosCopy(original.getPasos()));
        return copia;
    }

    private List<Paso> mutablePasosCopy(List<Paso> pasos) {
        List<Paso> copia = new ArrayList<>();
        if (pasos != null) {
            for (Paso paso : pasos) {
                copia.add(copyPaso(paso));
            }
        }
        return copia;
    }

    private Paso copyPaso(Paso original) {
        Paso copia = new Paso();
        copia.setId(original.getId());
        copia.setDescripcion(original.getDescripcion());
        return copia;
    }
}
