import { CanActivateFn, Router } from "@angular/router";
import { SessionStorageService } from "../service/session-storage.service";
import { inject } from "@angular/core";

export const loginGuard: CanActivateFn = () => { 

    const sessionStorageService: SessionStorageService = inject(SessionStorageService);
    const loginUser = sessionStorageService.getItem(SessionStorageService.LOGIN_USER);
    
    if (loginUser) {
        const router = inject(Router);
        router.navigate(['/dashboard']);
        return false;
    }
    return true;
}