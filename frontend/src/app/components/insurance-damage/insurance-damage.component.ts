import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { InsuranceDamageService, InsuranceDamage } from '../../services/insurance-damage.service';
import { Subscription } from 'rxjs';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

@Component({
  selector: 'app-insurance-damage',
  templateUrl: './insurance-damage.component.html',
  styleUrls: ['./insurance-damage.component.scss'],
  standalone: true,
  imports: [CommonModule, RouterModule, NgChartsModule],
  providers: [DatePipe]
})
export class InsuranceDamageComponent implements OnInit, OnDestroy {
  damages: InsuranceDamage[] = [];
  loading = true;
  error = false;
  private subscription: Subscription | null = null;

  // Chart properties
  activeChart: 'count' | 'amount' = 'count';

  barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    scales: {
      x: {
        ticks: {
          color: '#333'
        }
      },
      y: {
        beginAtZero: true,
        ticks: {
          color: '#333'
        }
      }
    },
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
      title: {
        display: true,
        text: 'Insurance Damage Reports'
      }
    }
  };

  barChartLabels: string[] = [];
  barChartData: ChartData<'bar'> = {
    labels: [],
    datasets: []
  };

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

          // Update chart data
          this.updateChartData();

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

  /**
   * Update chart data based on the active chart type
   */
  private updateChartData(): void {
    if (this.damages.length === 0) return;

    // Extract damage types for labels
    this.barChartLabels = this.damages.map(damage => damage.damageType);

    if (this.activeChart === 'count') {
      // Update chart data for count
      this.barChartData = {
        labels: this.barChartLabels,
        datasets: [{
          data: this.damages.map(damage => damage.count),
          label: 'Number of Reports',
          backgroundColor: 'rgba(54, 162, 235, 0.5)',
          borderColor: 'rgb(54, 162, 235)',
          borderWidth: 1
        }]
      };

      // Update chart title
      if (this.barChartOptions?.plugins?.title) {
        this.barChartOptions.plugins.title.text = 'Insurance Damage Reports - Count';
      }
    } else {
      // Update chart data for average amount
      this.barChartData = {
        labels: this.barChartLabels,
        datasets: [{
          data: this.damages.map(damage => damage.averageAmount),
          label: 'Average Payout Amount (€)',
          backgroundColor: 'rgba(255, 99, 132, 0.5)',
          borderColor: 'rgb(255, 99, 132)',
          borderWidth: 1
        }]
      };

      // Update chart title
      if (this.barChartOptions?.plugins?.title) {
        this.barChartOptions.plugins.title.text = 'Insurance Damage Reports - Average Amount';
      }
    }

    console.log('Chart data updated:', this.barChartData);
  }

  /**
   * Set the active chart type and update the chart
   */
  setActiveChart(chartType: 'count' | 'amount'): void {
    this.activeChart = chartType;
    this.updateChartData();
    this.cdr.detectChanges();
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
