import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Stock } from '../models/stock.model';

@Injectable({
  providedIn: 'root'
})
export class StockEventService {
  private stocksSubject = new BehaviorSubject<Stock[]>([]);
  private eventSource: EventSource | null = null;

  constructor() { }

  /**
   * Get the stocks as an observable
   */
  getStocks(): Observable<Stock[]> {
    return this.stocksSubject.asObservable();
  }

  /**
   * Connect to the event stream
   */
  connectToEventStream(): void {
    if (this.eventSource) {
      return; // Already connected
    }

    this.eventSource = new EventSource('/api/stocks/stream');

    this.eventSource.addEventListener('stock-update', (event: any) => {
      const newStocks = JSON.parse(event.data);
      console.log('Received stock updates:', newStocks);
      this.stocksSubject.next(newStocks);
    });

    this.eventSource.onerror = (error) => {
      console.error('EventSource error:', error);
      this.disconnectEventStream();
      // Try to reconnect after a delay
      setTimeout(() => this.connectToEventStream(), 5000);
    };
  }

  /**
   * Disconnect from the event stream
   */
  disconnectEventStream(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }
}
