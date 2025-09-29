import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { StocksComponent } from './components/stocks/stocks.component';
import { InsuranceDamageComponent } from './components/insurance-damage/insurance-damage.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'stocks', component: StocksComponent },
  { path: 'insurance', component: InsuranceDamageComponent },
  { path: '**', redirectTo: '' } // Redirect to home for any unknown routes
];
