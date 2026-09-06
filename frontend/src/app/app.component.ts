import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';

import { ProductionService } from './services/production.service';
import { ProductionAnalysis } from './models/production-analysis.model';
import { ProductionEvent } from './models/production-event.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatIconModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  private readonly productionService = inject(ProductionService);

  readonly productionId = 'test-production';

  events: ProductionEvent[] = [];
  analysis: ProductionAnalysis | null = null;

  loadingEvents = false;
  loadingAnalysis = false;
  error: string | null = null;

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.error = null;
    this.loadEvents();
    this.analyzeProduction();
  }

  loadEvents(): void {
    this.loadingEvents = true;

    this.productionService.getEvents()
      .pipe(
        finalize(() => this.loadingEvents = false)
      )
      .subscribe({
        next: events => {
          this.events = events;
        },
        error: () => {
          this.error = 'Unable to load production events.';
        }
      });
  }

  analyzeProduction(): void {
    this.loadingAnalysis = true;

    this.productionService
      .analyzeProduction(this.productionId)
      .pipe(
        finalize(() => this.loadingAnalysis = false)
      )
      .subscribe({
        next: analysis => {
          this.analysis = analysis;
        },
        error: () => {
          this.error = 'Unable to analyze the production.';
        }
      });
  }

  trackByEvent(index: number, event: ProductionEvent): string {
    return `${event.timestamp}-${event.scene_id}-${index}`;
  }
}
