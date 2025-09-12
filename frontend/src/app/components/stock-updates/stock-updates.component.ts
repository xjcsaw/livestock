import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StockHeaderComponent } from './stock-header/stock-header.component';
import { StockTableComponent } from './stock-table/stock-table.component';
import { LoadingMessageComponent } from './loading-message/loading-message-lit-wrapper.component';
import { Stock } from '../../models/stock.model';
import { StockEventService } from '../../services/stock-event.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-stock-updates',
  templateUrl: './stock-updates.component.html',
  styleUrls: ['./stock-updates.component.scss'],
  standalone: true,
  imports: [CommonModule, StockHeaderComponent, StockTableComponent, LoadingMessageComponent]
})
export class StockUpdatesComponent implements OnInit, OnDestroy {
  stocks: Stock[] = [];
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';
  private stockSubscription: Subscription | null = null;

  constructor(private stockEventService: StockEventService) { }

  ngOnInit(): void {
    this.stockEventService.connectToEventStream();
    this.stockSubscription = this.stockEventService.getStocks().subscribe(newStocks => {
      this.stocks = newStocks;
      if (this.sortColumn) {
        this.sortData(this.sortColumn);
      }
    });
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
    if (this.stockSubscription) {
      this.stockSubscription.unsubscribe();
    }
    this.stockEventService.disconnectEventStream();
  }
}
