import { Component } from '@angular/core';
import { StockUpdatesComponent } from './components/stock-updates/stock-updates.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  standalone: true,
  imports: [StockUpdatesComponent]
})
export class AppComponent {
  title = 'Livestock Management';
}
