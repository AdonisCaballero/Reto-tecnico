import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Receta } from '../models/receta';
import { CrearPasoRequest, Paso } from '../models/paso';

@Injectable({
  providedIn: 'root',
})
export class RecetaService {
  private readonly apiUrl = 'http://localhost:8080/recetas';
  private readonly pasosUrl = 'http://localhost:8080/pasos';

  constructor(private http: HttpClient) {}

  obtenerRecetas(): Observable<Receta[]> {
    return this.http.get<Receta[]>(this.apiUrl);
  }

  obtenerReceta(id: number): Observable<Receta> {
    return this.http.get<Receta>(`${this.apiUrl}/${id}`);
  }

  crearReceta(receta: Receta): Observable<Receta> {
    return this.http.post<Receta>(this.apiUrl, receta);
  }

  actualizarReceta(id: number, receta: Receta): Observable<Receta> {
    return this.http.put<Receta>(`${this.apiUrl}/${id}`, receta);
  }

  eliminarReceta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  obtenerPasos(recetaId: number): Observable<Paso[]> {
    return this.http.get<Paso[]>(`${this.apiUrl}/${recetaId}/pasos`);
  }

  crearPaso(recetaId: number, paso: Paso): Observable<Paso> {
    return this.http.post<Paso>(`${this.apiUrl}/${recetaId}/pasos`, paso);
  }

  actualizarPaso(recetaId: number, pasoId: number, paso: Paso): Observable<Paso> {
    return this.http.put<Paso>(`${this.apiUrl}/${recetaId}/pasos/${pasoId}`, paso);
  }

  eliminarPaso(recetaId: number, pasoId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${recetaId}/pasos/${pasoId}`);
  }

  crearPasoGlobal(request: CrearPasoRequest): Observable<Paso> {
    return this.http.post<Paso>(this.pasosUrl, request);
  }
}
