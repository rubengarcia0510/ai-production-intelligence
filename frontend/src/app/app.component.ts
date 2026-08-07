import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [MatButtonModule],
  template: `
    <div class="app-root">
      <header class="topbar">
        <h1>AI Production Intelligence</h1>
      </header>
      <main>
        <p>Welcome — frontend scaffold (Angular 20, standalone components, Material dark theme).</p>
        <button mat-raised-button color="primary" (click)="onPing()">Ping Backend</button>
      </main>
    </div>
  `,
  styles: [`
    :host { display: block; height: 100%; }
    .app-root { padding: 2rem; color: var(--foreground); background: var(--background); min-height:100vh }
    .topbar { margin-bottom: 1rem }
  `]
})
export class AppComponent {
  onPing(){
    fetch('/api/v1/ping').then(r=>r.text()).then(t=>alert(t)).catch(e=>alert('Could not reach backend'));
  }
}
