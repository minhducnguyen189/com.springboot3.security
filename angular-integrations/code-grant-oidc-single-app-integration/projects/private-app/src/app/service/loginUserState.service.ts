import { Injectable } from "@angular/core";
import { LoginUserResponse, LoginUserService } from "../rest-client";
import { Observable } from "rxjs";



@Injectable({
    providedIn: 'root'
})
export class LoginUserStateService {

    constructor(private loginUserService: LoginUserService) {

    }

    getCurrentLoginUser(): Observable<LoginUserResponse> {
        return this.loginUserService.getCurrentLoginUser();
    }

}