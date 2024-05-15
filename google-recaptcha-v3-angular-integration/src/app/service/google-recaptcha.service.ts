import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RecaptChaResponse } from '../model/recaptcha.model';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class GoogleRecaptchaService {

  constructor(private http: HttpClient) { }


  verifyRecaptchaToken(tokenValue: string): Observable<RecaptChaResponse> {
    return this.http.post<RecaptChaResponse>(`/v1/recaptcha/actions/verify`, {token: tokenValue});
  }

}
