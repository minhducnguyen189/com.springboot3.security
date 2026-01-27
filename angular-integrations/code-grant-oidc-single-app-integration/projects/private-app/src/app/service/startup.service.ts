import { Injectable } from "@angular/core";
import { SessionStorageService } from "./session-storage.service";
import { LoginUserStateService } from "./loginUserState.service";


@Injectable({
    providedIn: 'root'
})
export class StartupService {
    constructor(
        private _sessionStorageService: SessionStorageService,
        private _loginUserStateService: LoginUserStateService
    ) {}

    get loginUser(): string | null {
        return this._sessionStorageService.getItem(SessionStorageService.LOGIN_USER);
    }

    initApplication(): void {
        console.log("init application called");
        this._loginUserStateService.getCurrentLoginUser()
        .subscribe({
            next: user => {
                this._sessionStorageService.setItem(SessionStorageService.LOGIN_USER, JSON.stringify(user));
            },
            error: err => {
                console.warn("User not authenticated, skipping initialization", err);
            }
        });
    }
}