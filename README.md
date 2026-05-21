# Libro de Recetas de Minecraft

Aplicación full stack para gestionar recetas de crafteo de Minecraft: **Angular** (frontend) + **Spring Boot** (backend).

## Estructura del repositorio

```
.
├── frontend/          # Angular 21 (standalone, signals, formularios reactivos)
├── backend/           # Spring Boot 3.3 (API REST + persistencia JSON)
│   ├── data/
│   │   ├── recetas.json          # Datos en uso (se actualiza al usar la app)
│   │   └── datos-ejemplo.json    # Datos de ejemplo para cargar
│   └── sql/
│       ├── schema.sql            # Esquema relacional de referencia
│       └── datos-ejemplo.sql     # INSERT de ejemplo (referencia SQL)
├── README.md
├── iniciar-backend.ps1
├── iniciar-frontend.ps1
└── detener-backend.ps1
```

## Requisitos

| Herramienta | Versión |
|-------------|---------|
| Java JDK | 17+ |
| Maven | 3.8+ |
| Node.js | 20+ |
| npm | 10+ |

## Arranque rápido

### 1. Backend (puerto 8080)

Backend (puerto 8080)
PowerShell o CMD:
cd backend
mvn compile
mvn spring-boot:run

Espera el mensaje: `Started LibroDeRecetasApplication`.

### 2. Frontend (puerto 4200)

En **otra** terminal:

Frontend (puerto 4200)
Otra terminal (con el backend ya en marcha):

cd frontend
npm install
npm start

Abre: **http://localhost:4200**

### Scripts desde la raíz del repo

```powershell
.\detener-backend.ps1
.\iniciar-backend.ps1
.\iniciar-frontend.ps1
```

## Datos de ejemplo

La app guarda los datos en **`backend/data/recetas.json`**. Para cargar recetas de prueba:

```powershell
cd backend
.\cargar-datos-ejemplo.ps1
```

Reinicia el backend después. Incluye 3 recetas con pasos (espada, antorcha, mesa de crafteo).

También hay scripts SQL de referencia en `backend/sql/` (esquema y `INSERT` equivalentes).

## API REST

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/recetas` | Listar recetas |
| GET | `/recetas/{id}` | Detalle con pasos |
| POST | `/recetas` | Crear receta |
| PUT | `/recetas/{id}` | Editar receta |
| DELETE | `/recetas/{id}` | Borrar receta |
| GET | `/recetas/{id}/pasos` | Listar pasos |
| POST | `/recetas/{id}/pasos` | Añadir paso |
| PUT | `/recetas/{recetaId}/pasos/{pasoId}` | Editar paso |
| DELETE | `/recetas/{recetaId}/pasos/{pasoId}` | Eliminar paso |
| POST | `/pasos` | Crear paso (`{ descripcion, recetaId }`) |

## Funcionalidades

- Listar, crear, editar y borrar recetas
- Ver detalle de receta con sus pasos
- Añadir, editar y borrar pasos
- Buscador de recetas en el listado
- Persistencia en fichero JSON (los datos se mantienen al reiniciar el backend)

## Tecnologías

**Frontend:** Angular 21, standalone components, signals, reactive forms, HttpClient, routing.

**Backend:** Spring Boot 3.3, Jackson, persistencia en `recetas.json`.

---

## Problemas encontrados durante el desarrollo

### 1. Maven/npm ejecutados en la carpeta incorrecta

**Síntoma:** `No plugin found for prefix 'spring-boot'` o `Missing script: "start"`.

**Causa:** Se ejecutaba `mvn` o `npm` desde la raíz del repo en lugar de `backend/` o `frontend/`.

**Solución:** Entrar siempre en la carpeta correcta o usar los scripts `iniciar-backend.ps1` / `iniciar-frontend.ps1`.

---

### 2. Puerto 8080 ya en uso

**Síntoma:** `Web server failed to start. Port 8080 was already in use`.

**Causa:** Quedaba un proceso Java de un arranque anterior del backend.

**Solución:** Ejecutar `.\detener-backend.ps1` o cerrar la terminal con Ctrl+C antes de volver a arrancar.

---

### 3. Base de datos H2 bloqueada (`Database may be already in use`)

**Síntoma:** Error `90020` al iniciar Spring Boot con H2 en archivo.

**Causa:** Varias instancias del backend o H2 Console abiertas sobre el mismo fichero `.mv.db`.

**Solución:** Se migró la persistencia a **JSON** (`data/recetas.json`) para evitar bloqueos de H2 en desarrollo local.

---

### 4. Scripts PowerShell no encontrados

**Síntoma:** `.\iniciar-backend.ps1` no reconocido dentro de `backEnd`.

**Causa:** Los scripts estaban solo en la raíz del repo, no en la carpeta del módulo.

**Solución:** Se añadieron scripts también en `backend/` y `frontend/`.

---

### 5. Controller con lista en memoria vs servicios JPA

**Síntoma:** Los datos no persistían entre reinicios.

**Causa:** El `RecetaController` inicial guardaba recetas en un `ArrayList` en memoria, sin usar los repositorios.

**Solución:** Controllers conectados a servicios y almacenamiento en fichero JSON.

---

### 6. Pasos sin editar ni eliminar

**Síntoma:** Solo se podían añadir pasos, no modificarlos ni borrarlos.

**Solución:** Endpoints `PUT` y `DELETE` en `/recetas/{recetaId}/pasos/{pasoId}` y UI con botones Editar/Borrar en el detalle.

---

### 7. Build de Angular con SSR

**Síntoma:** Fallo de compilación por ficheros `main.server.ts` no incluidos en `tsconfig`.

**Solución:** Ajuste de `tsconfig.app.json` y `outputMode: static` en `angular.json` para build cliente simple.

---

### 8. Error al añadir pasos (404)

**Síntoma:** En el detalle de receta aparece "Error al añadir el paso" o 404 en POST `/recetas/{id}/pasos`.

**Causa:** El backend seguía corriendo con código antiguo (sin recompilar tras los cambios).

**Solución:**

```powershell
.\detener-backend.ps1
cd backend
.\iniciar-backend.ps1
```

Los scripts de arranque ahora ejecutan `mvn compile` antes de iniciar.

---

### 9. CORS y conexión frontend-backend

**Síntoma:** El frontend no cargaba datos si el backend no estaba en marcha.

**Solución:** `@CrossOrigin` en controllers y mensaje de error en la UI; el backend debe estar en `http://localhost:8080` antes de usar la app.

---

### 10. Reorganización de carpetas para la entrega

**Síntoma:** El enunciado pedía carpetas `frontend` y `backend` en la raíz.

**Causa:** El código vivía inicialmente en `LibroDeRecetas/frontEnd` y `LibroDeRecetas/backEnd`.

**Solución:** Proyecto reorganizado en `frontend/` y `backend/` en la raíz del repositorio.

---

## Subir a GitHub (repositorio público)

```powershell
git init
git add .
git commit -m "Entrega: Libro de Recetas Minecraft"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/TU_REPO.git
git push -u origin main
```


## Autores

DANICA LINDSAY VARGAS ARÉVALO 
Adonis Caballero alamo 
Pablo Rodríguez Gómez 
Alvaro Fernandez Montero
