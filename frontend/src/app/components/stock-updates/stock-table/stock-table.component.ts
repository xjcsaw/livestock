import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StockRowComponent } from '../stock-row/stock-row.component';

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
  selector: 'app-stock-table',
  templateUrl: './stock-table.component.html',
  styleUrls: ['./stock-table.component.scss'],
  standalone: true,
  imports: [CommonModule, StockRowComponent]
})
export class StockTableComponent {
  @Input() stocks: Stock[] = [];
  @Input() sortColumn: string = '';
  @Input() sortDirection: 'asc' | 'desc' = 'asc';
  @Output() sort = new EventEmitter<string>();

  sortData(column: string): void {
    this.sort.emit(column);
  }
}
