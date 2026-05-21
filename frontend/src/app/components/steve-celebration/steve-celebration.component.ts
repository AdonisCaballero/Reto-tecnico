import { Component, effect, input, output } from '@angular/core';

@Component({
  selector: 'app-steve-celebration',
  standalone: true,
  templateUrl: './steve-celebration.component.html',
  styleUrl: './steve-celebration.component.css',
})
export class SteveCelebrationComponent {
  visible = input(false);
  recetaNombre = input('');

  closed = output<void>();

  private autoCloseTimer: ReturnType<typeof setTimeout> | null = null;

  constructor() {
    effect(() => {
      if (this.autoCloseTimer) {
        clearTimeout(this.autoCloseTimer);
        this.autoCloseTimer = null;
      }

      if (this.visible()) {
        this.autoCloseTimer = setTimeout(() => this.cerrar(), 5000);
      }
    });
  }

  cerrar(): void {
    if (this.autoCloseTimer) {
      clearTimeout(this.autoCloseTimer);
      this.autoCloseTimer = null;
    }
    this.closed.emit();
  }
}
