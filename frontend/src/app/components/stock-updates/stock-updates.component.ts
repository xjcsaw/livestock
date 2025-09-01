import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-stock-updates',
  templateUrl: './stock-updates.component.html',
  styleUrls: ['./stock-updates.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class StockUpdatesComponent implements OnInit, OnDestroy {
  stockData: { symbol: string, price: number } | null = null;
  eventSource: EventSource | null = null;

  constructor() { }

  ngOnInit(): void {
    this.connectToEventStream();
  }

  ngOnDestroy(): void {
    this.disconnectEventStream();
  }

  connectToEventStream(): void {
    this.eventSource = new EventSource('/api/stocks/stream');
    
    this.eventSource.addEventListener('stock-update', (event: any) => {
      this.stockData = JSON.parse(event.data);
      console.log('Received stock update:', this.stockData);
    });

    this.eventSource.onerror = (error) => {
      console.error('EventSource error:', error);
      this.disconnectEventStream();
      // Try to reconnect after a delay
      setTimeout(() => this.connectToEventStream(), 5000);
    };
  }

  disconnectEventStream(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }
}
