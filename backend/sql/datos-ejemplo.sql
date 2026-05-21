-- Datos de ejemplo (referencia SQL)
-- Para cargar en la app: copia backend/data/datos-ejemplo.json a backend/data/recetas.json
-- o ejecuta: .\cargar-datos-ejemplo.ps1

DELETE FROM pasos;
DELETE FROM recetas;

INSERT INTO recetas (id, nombre) VALUES
(1, 'Espada de diamante'),
(2, 'Antorcha'),
(3, 'Mesa de crafteo');

INSERT INTO pasos (id, descripcion, receta_id) VALUES
(1, 'Consigue 2 palos y 1 diamante.', 1),
(2, 'Abre la mesa de crafteo 3x3.', 1),
(3, 'Coloca el diamante en el centro y los palos debajo.', 1),
(4, 'Craftea 4 palos y 1 carbón.', 2),
(5, 'Coloca el carbón sobre el palo en la mesa 2x2.', 2),
(6, 'Coloca 4 tablones de madera en un cuadrado 2x2.', 3);
