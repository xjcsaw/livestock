import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Stock {
  symbol: string;
  price: number;
  priceChange: number;
  priceChangePercent: number;
  high24h: number;
  low24h: number;
  volume: number;
  lastUpdate: string;
}

@Component({
  selector: 'app-stock-updates',
  templateUrl: './stock-updates.component.html',
  styleUrls: ['./stock-updates.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class StockUpdatesComponent implements OnInit, OnDestroy {
  stocks: Stock[] = [];
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
      this.stocks = JSON.parse(event.data);
      console.log('Received stock updates:', this.stocks);
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
