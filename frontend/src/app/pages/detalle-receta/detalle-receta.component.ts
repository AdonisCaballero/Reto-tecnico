import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { RecetaService } from '../../services/receta.service';
import { Receta } from '../../models/receta';
import { Paso } from '../../models/paso';

@Component({
  selector: 'app-detalle-receta',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './detalle-receta.component.html',
  styleUrl: './detalle-receta.component.css',
})
export class DetalleRecetaComponent implements OnInit {
  private readonly recetaService = inject(RecetaService);
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);

  receta = signal<Receta | null>(null);
  pasos = signal<Paso[]>([]);
  mensaje = signal('');
  recetaId = signal<number>(0);
  pasoEditandoId = signal<number | null>(null);

  form = this.fb.group({
    descripcion: ['', Validators.required],
  });

  editForm = this.fb.group({
    nombre: ['', Validators.required],
  });

  editPasoForm = this.fb.group({
    descripcion: ['', Validators.required],
  });

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.recetaId.set(id);
    this.cargarDetalle(id);
  }

  cargarDetalle(id: number): void {
    this.recetaService.obtenerReceta(id).subscribe({
      next: (receta) => {
        this.receta.set(receta);
        this.editForm.patchValue({ nombre: receta.nombre });
        this.pasos.set(receta.pasos ?? []);
      },
      error: () => this.mensaje.set('No se pudo cargar la receta.'),
    });

    this.recetaService.obtenerPasos(id).subscribe({
      next: (pasos) => this.pasos.set(pasos),
      error: () => {},
    });
  }

  guardarPaso(): void {
    if (this.form.invalid) {
      return;
    }

    const descripcion = this.form.value.descripcion?.trim() ?? '';
    const recetaId = this.recetaId();

    this.recetaService.crearPaso(recetaId, { descripcion }).subscribe({
      next: (paso) => {
        this.pasos.update((lista) => [...lista, paso]);
        this.form.reset();
        this.mensaje.set('Paso añadido.');
      },
      error: (err) => {
        if (err.status === 404) {
          this.mensaje.set(
            'El backend no tiene la ruta POST /pasos. Detenlo (detener-backend.ps1) y vuelve a iniciarlo.',
          );
        } else if (err.status === 0) {
          this.mensaje.set('No hay conexion con el backend (puerto 8080).');
        } else {
          this.mensaje.set('Error al añadir el paso. Reinicia el backend e intentalo de nuevo.');
        }
      },
    });
  }

  iniciarEdicionPaso(paso: Paso): void {
    this.pasoEditandoId.set(paso.id!);
    this.editPasoForm.patchValue({ descripcion: paso.descripcion });
  }

  cancelarEdicionPaso(): void {
    this.pasoEditandoId.set(null);
    this.editPasoForm.reset();
  }

  guardarEdicionPaso(): void {
    if (this.editPasoForm.invalid || this.pasoEditandoId() === null) {
      return;
    }

    const pasoId = this.pasoEditandoId()!;
    const descripcion = this.editPasoForm.value.descripcion?.trim() ?? '';
    const recetaId = this.recetaId();

    this.recetaService.actualizarPaso(recetaId, pasoId, { descripcion }).subscribe({
      next: (paso) => {
        this.pasos.update((lista) =>
          lista.map((p) => (p.id === pasoId ? paso : p)),
        );
        this.cancelarEdicionPaso();
        this.mensaje.set('Paso actualizado.');
      },
      error: () => this.mensaje.set('Error al actualizar el paso.'),
    });
  }

  eliminarPaso(pasoId: number): void {
    const recetaId = this.recetaId();

    this.recetaService.eliminarPaso(recetaId, pasoId).subscribe({
      next: () => {
        this.pasos.update((lista) => lista.filter((p) => p.id !== pasoId));
        if (this.pasoEditandoId() === pasoId) {
          this.cancelarEdicionPaso();
        }
        this.mensaje.set('Paso eliminado.');
      },
      error: () => this.mensaje.set('Error al eliminar el paso.'),
    });
  }

  guardarReceta(): void {
    if (this.editForm.invalid) {
      return;
    }

    const nombre = this.editForm.value.nombre?.trim() ?? '';
    const id = this.recetaId();

    this.recetaService.actualizarReceta(id, { nombre }).subscribe({
      next: (receta) => {
        this.receta.set(receta);
        this.mensaje.set('Receta actualizada.');
      },
      error: () => this.mensaje.set('Error al actualizar la receta.'),
    });
  }
}
