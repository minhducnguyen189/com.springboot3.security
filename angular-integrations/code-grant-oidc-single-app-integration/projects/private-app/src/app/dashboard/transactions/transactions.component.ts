import { Component, OnInit } from '@angular/core';
import { TransactionDashboardService } from '../../service/transaction.service';
import { TransactionDetail } from '../../rest-client/model/transactionDetail';
import { SortOrderEnum, TransactionFilterResponse } from '../../rest-client';
import { HttpErrorResponse } from '@angular/common/http';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent implements OnInit {

  transactions: TransactionDetail[] = [];
  pageSize: number | 'ALL' = 50;
  pageIndex: number = 0;
  totalItems: number = 0;
  loading: boolean = false;
  loadingTime: number | null = null;
  useCursorFilter: boolean = false;
  nextPageToken: number = 0;
  previousPageToken: number = 0;

  constructor(
    private _transactionService: TransactionDashboardService
  ) {}

  ngOnInit() {
    this.loadTransactions();
  }


  get pageSizeValue(): number {
    return this.pageSize === 'ALL' ? 150 : this.pageSize as number;
  }

  loadTransactions(cursorToken?: { next?: number, prev?: number }) {
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
        pageNumber: cursorToken ? 0 : this.pageIndex,
        pageSize: this.pageSizeValue,
        sortOrder: SortOrderEnum.Asc,
        sortBy: "sequenceNumber",
        previousPageToken: cursorToken?.prev,
        nextPageToken: cursorToken?.next
      }
    };

    const apiCall = this.useCursorFilter 
      ? this._transactionService.filterTransactionsWithCursor(filterRequest)
      : this._transactionService.filteredTransactions(filterRequest);

    apiCall.subscribe({
      next: (response: TransactionFilterResponse) => {
        if (this.pageSize === 'ALL' && this.pageIndex > 0) {
           this.transactions = [...this.transactions, ...(response.data || [])];
        } else {
           this.transactions = response.data || [];
        }
        this.totalItems = response.totalItems ?? 0;
        this.nextPageToken = response.nextPageToken ?? 0;
        this.previousPageToken = response.previousPageToken ?? 0;
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
    this.pageIndex = 0; 
    this.pageSize = 50;
    this.loadTransactions();
  }

  onPageChange(event?: PageEvent) {
    const previousPageIndex = this.pageIndex;
    this.pageIndex = event?.pageIndex || 0;
    
    // 150 is the hardcoded virtual page size in TableComponent.
    // If we receive 150, treat it as 'ALL' mode to ensure appending logic works.
    if (event?.pageSize === 150) {
      this.pageSize = 'ALL';
    } else {
      this.pageSize = event?.pageSize || 50;
    }

    if (this.useCursorFilter) {
      if (this.pageIndex > previousPageIndex) {
        this.loadTransactions({ next: this.nextPageToken });
      } else if (this.pageIndex < previousPageIndex) {
        this.loadTransactions({ prev: this.previousPageToken });
      } else {
        this.loadTransactions();
      }
    } else {
      this.loadTransactions();
    }
  }

}