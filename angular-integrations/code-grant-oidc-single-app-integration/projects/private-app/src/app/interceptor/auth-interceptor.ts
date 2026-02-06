import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { catchError, Observable, throwError } from "rxjs";
import { StartupService } from "../service/startup.service";
import { MatDialog } from "@angular/material/dialog";
import { RefreshTokenDialogComponent } from "../refresh-token-dialog/refresh-token-dialog.component";

@Injectable({
    providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

    isRefreshingToken: boolean = false;

    constructor(
        private _router: Router,
        private _dialog: MatDialog,
        private _startupService: StartupService,
    ) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401) {
                   this._handleUnauthorizedError(req);
                }
                if (error.status === 403) {
                    this._router.navigate(['error-page/code=403&message=Forbidden']);
                }
                return throwError(() => error);
            })
        );
    }

    private _handleUnauthorizedError(req: HttpRequest<object>): void {
        const errorPath = encodeURIComponent(window.location.pathname);
        if (!this._startupService.loginUser) {
            this._navigateToLogin(errorPath);
        } else {
            if (!this.isRefreshingToken) {
                this.isRefreshingToken = true;
                const apiError: ApiError = {
                    errorMethod: req.method,
                    errorPath: errorPath
                }
                this._openRefreshTokenDialog(apiError);
            }
        }
    }


    private _navigateToLogin(errorPath: string): void {
        if (!errorPath.includes('login')) {
            this._router.navigate(['login'], { queryParams: {errorPath: errorPath}});
        }
    }

    private _openRefreshTokenDialog(apiError: ApiError) {
        const dialogRef = this._dialog.open(RefreshTokenDialogComponent, {
            data: apiError,
        });
        dialogRef.afterClosed().subscribe(() => {
            this.isRefreshingToken = false;
        });
    }


}


export interface ApiError {
    errorMethod: string;
    errorPath: string;
}
