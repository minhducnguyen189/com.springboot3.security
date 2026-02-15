import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BankAccountService, BankAccountFilterRequest, BankAccountFilterResponse } from '../rest-client';

@Injectable({
  providedIn: 'root'
})
export class BankAccountDashboardService {

  constructor(private bankAccountService: BankAccountService) { }

  /**
   * Get filtered bank accounts
   * @param filter Optional filter criteria
   * @returns Observable of bank account filter response
   */
  filterBankAccounts(filter: BankAccountFilterRequest): Observable<BankAccountFilterResponse> {
    return this.bankAccountService.filterBankAccounts(filter);
  }

  /**
   * Get filtered bank accounts using cursor pagination
   * @param filter Optional filter criteria
   * @returns Observable of bank account filter response
   */
  filterBankAccountsWithCursor(filter: BankAccountFilterRequest): Observable<BankAccountFilterResponse> {
    return this.bankAccountService.filterBankAccountsWithCursor(filter);
  }

}
