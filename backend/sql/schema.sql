-- Esquema de referencia (modelo relacional 1:N)
-- La aplicación persiste en JSON (backend/data/recetas.json).
-- Este script documenta la estructura equivalente en SQL.

CREATE TABLE IF NOT EXISTS recetas (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS pasos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(500) NOT NULL,
    receta_id   INT NOT NULL,
    CONSTRAINT fk_pasos_receta
        FOREIGN KEY (receta_id) REFERENCES recetas(id)
        ON DELETE CASCADE
);
