import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { InsuranceDamageService, InsuranceDamage } from '../../services/insurance-damage.service';
import { Subscription } from 'rxjs';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-insurance-damage',
  templateUrl: './insurance-damage.component.html',
  styleUrls: ['./insurance-damage.component.scss'],
  standalone: true,
  imports: [CommonModule, RouterModule],
  providers: [DatePipe]
})
export class InsuranceDamageComponent implements OnInit, OnDestroy {
  damages: InsuranceDamage[] = [];
  loading = true;
  error = false;
  private subscription: Subscription | null = null;

  constructor(
    private insuranceDamageService: InsuranceDamageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.error = false;

    // Subscribe to insurance damage updates
    console.log('Component subscribing to insurance damage updates...');
    this.subscription = this.insuranceDamageService.getInsuranceDamages()
      .subscribe({
        next: (damages) => {
          console.log('Component received damages update:', damages);
          // Create a new array reference to ensure Angular's change detection recognizes the change
          this.damages = [...damages];
          console.log('Component damages array updated:', this.damages);
          this.loading = false;
          // Explicitly trigger change detection
          this.cdr.detectChanges();
          console.log('Change detection triggered');
        },
        error: (err) => {
          console.error('Error subscribing to insurance damages:', err);
          this.error = true;
          this.loading = false;
        }
      });

    // Fetch initial data and connect to stream
    this.insuranceDamageService.fetchAllDamages();
    this.insuranceDamageService.connectToEventStream();
  }

  ngOnDestroy(): void {
    // Clean up subscription and disconnect from stream
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.insuranceDamageService.disconnectEventStream();
  }

  // Format currency values
  formatCurrency(value: number): string {
    return new Intl.NumberFormat('de-DE', { 
      style: 'currency', 
      currency: 'EUR' 
    }).format(value);
  }
}
