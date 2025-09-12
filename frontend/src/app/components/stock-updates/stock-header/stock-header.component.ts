import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-stock-header',
  templateUrl: './stock-header.component.html',
  styleUrls: ['./stock-header.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class StockHeaderComponent {
  // This component is responsible for displaying the header of the stock updates section
}
