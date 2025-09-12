import {Component, input, Input} from '@angular/core';
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
  readonly stock = input<Stock>({
    symbol: '',
    price: 0,
    priceChange: 0,
    priceChangePercent: 0,
    high24h: 0,
    low24h: 0,
    volume: 0,
    lastUpdate: ''
  });
}
