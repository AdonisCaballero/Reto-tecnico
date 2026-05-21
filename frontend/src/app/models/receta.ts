import { Paso } from './paso';

export interface Receta {
  id?: number;
  nombre: string;
  pasos?: Paso[];
}
