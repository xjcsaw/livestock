import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { StockUpdatesComponent } from '../stock-updates/stock-updates.component';

@Component({
  selector: 'app-stocks',
  templateUrl: './stocks.component.html',
  styleUrls: ['./stocks.component.scss'],
  standalone: true,
  imports: [RouterModule, StockUpdatesComponent]
})
export class StocksComponent {
  // This component is just a wrapper for the stock-updates component
}
