export interface Paso {
  id?: number;
  descripcion: string;
}

export interface CrearPasoRequest {
  descripcion: string;
  recetaId: number;
}
