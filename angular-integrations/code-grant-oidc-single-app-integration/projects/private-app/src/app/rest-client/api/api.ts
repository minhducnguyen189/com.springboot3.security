export * from './authentication.service';
import { AuthenticationService } from './authentication.service';
export * from './loginUser.service';
import { LoginUserService } from './loginUser.service';
export * from './transaction.service';
import { TransactionService } from './transaction.service';
export const APIS = [AuthenticationService, LoginUserService, TransactionService];
