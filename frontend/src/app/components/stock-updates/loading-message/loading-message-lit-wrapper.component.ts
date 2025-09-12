import { Component, OnInit, ElementRef, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import 'loading-message-lit';

@Component({
  selector: 'app-loading-message',
  template: '<loading-message-lit></loading-message-lit>',
  standalone: true,
  imports: [CommonModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoadingMessageComponent implements OnInit {
  constructor(private el: ElementRef) {}

  ngOnInit() {
    // Ensure the custom element is defined
    if (!customElements.get('loading-message-lit')) {
      console.warn('loading-message-lit custom element not defined. Make sure the library is properly loaded.');
    }
  }
}
