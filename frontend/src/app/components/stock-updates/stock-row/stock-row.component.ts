import { Component, Input } from '@angular/core';
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
  selector: 'app-stock-row',
  templateUrl: './stock-row.component.html',
  styleUrls: ['./stock-row.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class StockRowComponent {
  @Input() stock!: Stock;
}
