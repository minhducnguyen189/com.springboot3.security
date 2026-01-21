import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TransactionService, TransactionFilterRequest, TransactionFilterResponse } from '../rest-client';
import { TransactionDetail } from '../rest-client/model/transactionDetail';

@Injectable({
  providedIn: 'root'
})
export class TransactionDashboardService {

  constructor(private transactionService: TransactionService) {}

  /**
   * Get filtered transactions
   * @param filter Optional filter criteria
   * @returns Observable of transaction filter response
   */
  filteredTransactions(filter: TransactionFilterRequest): Observable<TransactionFilterResponse> {
    return this.transactionService.filterTransactions(filter);
  }

  /**
   * Get filtered transactions using cursor pagination
   * @param filter Optional filter criteria
   * @returns Observable of transaction filter response
   */
  filterTransactionsWithCursor(filter: TransactionFilterRequest): Observable<TransactionFilterResponse> {
    return this.transactionService.filterTransactionsWithCursor(filter);
  }

}