import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loading-message',
  templateUrl: './loading-message.component.html',
  styleUrls: ['./loading-message.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class LoadingMessageComponent {
  // This component displays a loading message when waiting for stock data
}
