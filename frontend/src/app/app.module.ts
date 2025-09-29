import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule, DatePipe } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { StocksComponent } from './components/stocks/stocks.component';
import { InsuranceDamageComponent } from './components/insurance-damage/insurance-damage.component';

// Import the stock-updates component and its sub-components
import { StockUpdatesComponent } from './components/stock-updates/stock-updates.component';
import { StockHeaderComponent } from './components/stock-updates/stock-header/stock-header.component';
import { StockRowComponent } from './components/stock-updates/stock-row/stock-row.component';
import { StockTableComponent } from './components/stock-updates/stock-table/stock-table.component';
import { LoadingMessageComponent } from './components/stock-updates/loading-message/loading-message.component';

// Import services
import { StockEventService } from './services/stock-event.service';
import { InsuranceDamageService } from './services/insurance-damage.service';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    StocksComponent,
    InsuranceDamageComponent,
    StockUpdatesComponent,
    StockHeaderComponent,
    StockRowComponent,
    StockTableComponent,
    LoadingMessageComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    CommonModule,
    AppRoutingModule
  ],
  providers: [
    StockEventService,
    InsuranceDamageService,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
