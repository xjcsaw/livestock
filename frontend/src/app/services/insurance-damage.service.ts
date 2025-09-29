import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

// Define the InsuranceDamage model
export interface InsuranceDamage {
  id: string;
  damageType: string;
  count: number;
  averageAmount: number;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class InsuranceDamageService {
  // BehaviorSubject to store and emit insurance damages
  private damagesSubject = new BehaviorSubject<InsuranceDamage[]>([]);
  private eventSource: EventSource | null = null;

  constructor() { }

  /**
   * Get the insurance damages as an observable
   */
  getInsuranceDamages(): Observable<InsuranceDamage[]> {
    return this.damagesSubject.asObservable();
  }

  /**
   * Connect to the event stream
   */
  connectToEventStream(): void {
    if (this.eventSource) {
      return; // Already connected
    }

    this.eventSource = new EventSource('/api/insurance/stream');

    this.eventSource.onmessage = (event: MessageEvent) => {
      const data = JSON.parse(event.data);
      console.log('Raw event data received:', data);

      // Ensure numeric values are properly parsed
      const newDamage = this.parseInsuranceDamage(data);
      console.log('Received insurance damage update:', newDamage);

      // Update the current list
      const currentDamages = this.damagesSubject.value;
      console.log('Current damages before update:', currentDamages);

      const index = currentDamages.findIndex(d => d.id === newDamage.id);
      console.log(`Finding damage with id ${newDamage.id}, found at index: ${index}`);

      // Create a completely new array to ensure change detection
      let updatedDamages: InsuranceDamage[];

      if (index >= 0) {
        // Update existing item
        console.log(`Updating existing damage at index ${index}:`, currentDamages[index], 'with new data:', newDamage);
        updatedDamages = currentDamages.map((item: InsuranceDamage, i: number) => 
          i === index ? { ...newDamage } : { ...item }
        );
        console.log('Updated damages list:', updatedDamages);
      } else {
        // Add new item
        console.log('Adding new damage to list:', newDamage);
        updatedDamages = [...currentDamages.map((item: InsuranceDamage) => ({ ...item })), { ...newDamage }];
        console.log('Updated damages list with new item:', updatedDamages);
      }

      // Emit the new array to all subscribers with a guaranteed new reference
      this.damagesSubject.next([...updatedDamages]);
      console.log('Emitted new array reference to subscribers');
    };

    this.eventSource.onerror = (error) => {
      console.error('EventSource error:', error);
      this.disconnectEventStream();
      // Try to reconnect after a delay
      setTimeout(() => this.connectToEventStream(), 5000);
    };
  }

  /**
   * Fetch all insurance damages
   */
  fetchAllDamages(): void {
    console.log('Fetching all insurance damages...');
    fetch('/api/insurance')
      .then(response => {
        console.log('Received response from /api/insurance:', response.status);
        return response.json();
      })
      .then(data => {
        console.log('Raw fetched insurance damages:', data);
        // Ensure numeric values are properly parsed
        const parsedData = data.map((item: any) => this.parseInsuranceDamage(item));
        console.log('Parsed insurance damages:', parsedData);

        // Create completely new object references for each item to ensure change detection
        const newData = parsedData.map((item: InsuranceDamage) => ({ ...item }));
        console.log('Created new object references for each item:', newData);

        // Force a new array reference even if the data is the same
        const currentData = this.damagesSubject.value;
        if (JSON.stringify(currentData) === JSON.stringify(newData)) {
          console.log('Data is the same, but forcing new array reference for change detection');
        }

        // Emit the new array to all subscribers
        this.damagesSubject.next([...newData]);
        console.log('Updated damagesSubject with parsed data');
      })
      .catch(error => {
        console.error('Error fetching insurance damages:', error);
      });
  }

  /**
   * Parse insurance damage data to ensure numeric values are properly handled
   */
  private parseInsuranceDamage(data: any): InsuranceDamage {
    // Always use damageType as the id for consistency
    const parsedDamage = {
      id: data.damageType,
      damageType: data.damageType,
      count: data.newReportsCount || 0,
      averageAmount: parseFloat(data.averagePayoutAmount) || 0,
      timestamp: data.lastUpdate
    };
    console.log('Parsed damage data:', parsedDamage);
    return parsedDamage;
  }

  /**
   * Disconnect from the event stream
   */
  disconnectEventStream(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }
}
