import { Component, OnInit } from '@angular/core';
import { BankAccountDashboardService } from '../../service/bank-account.service';
import { BankAccountDetail } from '../../rest-client/model/bankAccountDetail';
import { SortOrderEnum, BankAccountFilterResponse } from '../../rest-client';
import { HttpErrorResponse } from '@angular/common/http';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  bankAccounts: BankAccountDetail[] = [];
  pageSize: number | 'ALL' = 50;
  pageIndex: number = 0;
  totalItems: number = 0;
  loading: boolean = false;
  loadingTime: number | null = null;
  useCursorFilter: boolean = false;
  nextPageToken: number = 0;
  previousPageToken: number = 0;

  constructor(
    private _bankAccountService: BankAccountDashboardService
  ) { }

  ngOnInit() {
    this.loadBankAccounts();
  }


  get pageSizeValue(): number {
    return this.pageSize === 'ALL' ? 150 : this.pageSize as number;
  }

  loadBankAccounts(cursorToken?: { next?: number, prev?: number }) {
    this.loading = true;
    this.loadingTime = null;
    const startTime = performance.now();

    const filterRequest = {
      firstName: undefined,
      lastName: undefined,
      phone: undefined,
      email: undefined,
      accountNumber: undefined,
      accountType: undefined,
      ifscCode: undefined,
      status: undefined,
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
      ? this._bankAccountService.filterBankAccountsWithCursor(filterRequest)
      : this._bankAccountService.filterBankAccounts(filterRequest);

    apiCall.subscribe({
      next: (response: BankAccountFilterResponse) => {
        if (this.pageSize === 'ALL' && this.pageIndex > 0) {
          this.bankAccounts = [...this.bankAccounts, ...(response.data || [])];
        } else {
          this.bankAccounts = response.data || [];
        }
        this.totalItems = response.totalItems ?? 0;
        this.nextPageToken = response.nextPageToken ?? 0;
        this.previousPageToken = response.previousPageToken ?? 0;
        this.loading = false;
        this.loadingTime = performance.now() - startTime;
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error loading bank accounts:', error);
        this.loading = false;
        this.loadingTime = performance.now() - startTime;
      }
    });
  }

  onFilterModeChange(event: any) {
    this.useCursorFilter = event.checked;
    this.pageIndex = 0;
    this.pageSize = 50;
    this.loadBankAccounts();
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
        this.loadBankAccounts({ next: this.nextPageToken });
      } else if (this.pageIndex < previousPageIndex) {
        this.loadBankAccounts({ prev: this.previousPageToken });
      } else {
        this.loadBankAccounts();
      }
    } else {
      this.loadBankAccounts();
    }
  }

}
