import { Routes } from '@angular/router';
import { RecetasComponent } from './pages/recetas/recetas.component';
import { DetalleRecetaComponent } from './pages/detalle-receta/detalle-receta.component';

export const routes: Routes = [
  {
    path: '',
    component: RecetasComponent,
  },
  {
    path: 'recetas/:id',
    component: DetalleRecetaComponent,
  },
];
