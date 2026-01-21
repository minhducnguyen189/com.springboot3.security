import { Component, OnInit } from '@angular/core';
import { TransactionDashboardService } from '../../service/transaction.service';
import { TransactionDetail } from '../../rest-client/model/transactionDetail';
import { TransactionFilterResponse } from '../../rest-client';
import { HttpErrorResponse } from '@angular/common/http';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent implements OnInit {

  // Transactions data
  transactions: TransactionDetail[] = [];
  pageSize: number = 50;
  pageIndex: number = 0;
  totalItems: number = 0;
  loading: boolean = false;
  loadingTime: number | null = null;
  useCursorFilter: boolean = false;

  constructor(
    private _transactionService: TransactionDashboardService
  ) {}

  ngOnInit() {
    this.loadTransactions();
  }

  loadTransactions() {
    this.loading = true;
    this.loadingTime = null;
    const startTime = performance.now();
    
    const filterRequest = {
      date: undefined,
      domain: undefined,
      location: undefined,
      value: undefined,
      status: undefined,
      paymentMethod: undefined,
      taxAmount: undefined,
      netValue: undefined,
      pagination: {
        pageNumber: this.pageIndex + 1,
        pageSize: this.pageSize,
        sortOrder: undefined,
        sortBy: undefined,
      }
    };

    const apiCall = this.useCursorFilter 
      ? this._transactionService.filterTransactionsWithCursor(filterRequest)
      : this._transactionService.filteredTransactions(filterRequest);

    apiCall.subscribe({
      next: (response: TransactionFilterResponse) => {
        this.transactions = response.data || [];
        this.totalItems = response.totalItems ?? 0;
        this.loading = false;
        this.loadingTime = performance.now() - startTime;
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error loading transactions:', error);
        this.loading = false;
        this.loadingTime = performance.now() - startTime;
      }
    });
  }

  onFilterModeChange(event: any) {
    this.useCursorFilter = event.checked;
    // Reset pagination when switching modes as they might behave differently
    this.pageIndex = 0; 
    this.loadTransactions();
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadTransactions();
  }

}