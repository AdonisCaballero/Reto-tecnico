import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { RecetaService } from '../../services/receta.service';
import { Receta } from '../../models/receta';

@Component({
  selector: 'app-recetas',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './recetas.component.html',
  styleUrl: './recetas.component.css',
})
export class RecetasComponent implements OnInit {
  private readonly recetaService = inject(RecetaService);
  private readonly fb = inject(FormBuilder);

  recetas = signal<Receta[]>([]);
  busqueda = signal('');
  mensaje = signal('');
  cargando = signal(false);

  recetasFiltradas = computed(() => {
    const termino = this.busqueda().trim().toLowerCase();
    const lista = this.recetas();
    if (!termino) {
      return lista;
    }
    return lista.filter((r) => r.nombre.toLowerCase().includes(termino));
  });

  form = this.fb.group({
    nombre: ['', Validators.required],
  });

  ngOnInit(): void {
    this.cargarRecetas();
  }

  cargarRecetas(): void {
    this.cargando.set(true);
    this.recetaService.obtenerRecetas().subscribe({
      next: (data) => {
        this.recetas.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.mensaje.set('No se pudo conectar con el backend (puerto 8080).');
        this.cargando.set(false);
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      return;
    }

    const nombre = this.form.value.nombre?.trim() ?? '';
    this.recetaService.crearReceta({ nombre }).subscribe({
      next: (receta) => {
        this.recetas.update((lista) => [...lista, receta]);
        this.form.reset();
        this.mensaje.set(`Receta "${receta.nombre}" creada.`);
      },
      error: () => this.mensaje.set('Error al crear la receta.'),
    });
  }

  eliminar(id: number): void {
    this.recetaService.eliminarReceta(id).subscribe({
      next: () => {
        this.recetas.update((lista) => lista.filter((r) => r.id !== id));
        this.mensaje.set('Receta eliminada.');
      },
      error: () => this.mensaje.set('Error al eliminar la receta.'),
    });
  }
}
