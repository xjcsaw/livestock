import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StockHeaderComponent } from './stock-header/stock-header.component';
import { StockTableComponent } from './stock-table/stock-table.component';
import { LoadingMessageComponent } from './loading-message/loading-message.component';

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
  imports: [CommonModule, StockHeaderComponent, StockTableComponent, LoadingMessageComponent]
})
export class StockUpdatesComponent implements OnInit, OnDestroy {
  stocks: Stock[] = [];
  eventSource: EventSource | null = null;
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor() { }

  ngOnInit(): void {
    this.connectToEventStream();
  }

  sortData(column: string): void {
    // If clicking the same column, toggle sort direction
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      // New column, default to ascending
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }

    // Sort the stocks array
    this.stocks = [...this.stocks].sort((a, b) => {
      const valueA = a[column as keyof Stock];
      const valueB = b[column as keyof Stock];

      // Handle string comparison separately
      if (typeof valueA === 'string' && typeof valueB === 'string') {
        return this.sortDirection === 'asc' 
          ? valueA.localeCompare(valueB) 
          : valueB.localeCompare(valueA);
      }

      // Handle numeric comparison
      const comparison = valueA < valueB ? -1 : valueA > valueB ? 1 : 0;
      return this.sortDirection === 'asc' ? comparison : -comparison;
    });
  }

  ngOnDestroy(): void {
    this.disconnectEventStream();
  }

  connectToEventStream(): void {
    this.eventSource = new EventSource('/api/stocks/stream');

    this.eventSource.addEventListener('stock-update', (event: any) => {
      const newStocks = JSON.parse(event.data);
      console.log('Received stock updates:', newStocks);

      // Update stocks and maintain current sort if any
      this.stocks = newStocks;
      if (this.sortColumn) {
        this.sortData(this.sortColumn);
      }
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
