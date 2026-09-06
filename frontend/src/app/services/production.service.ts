import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductionAnalysis } from '../models/production-analysis.model';
import { ProductionEvent } from '../models/production-event.model';

@Injectable({
  providedIn: 'root'
})
export class ProductionService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/v1';

  getEvents(): Observable<ProductionEvent[]> {
    return this.http.get<ProductionEvent[]>(
      `${this.apiUrl}/events`
    );
  }

  analyzeProduction(productionId: string): Observable<ProductionAnalysis> {
    const params = new HttpParams()
      .set('productionId', productionId);

    return this.http.get<ProductionAnalysis>(
      `${this.apiUrl}/analysis`,
      { params }
    );
  }
}
