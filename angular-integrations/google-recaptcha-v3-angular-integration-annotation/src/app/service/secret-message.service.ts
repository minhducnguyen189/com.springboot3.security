import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SecretMessage {

  constructor(private http: HttpClient) { }



  getSecretMessage(tokenValue: string): Observable<string> {
    const headers = new HttpHeaders().set('google-recaptcha-response', tokenValue);
    return this.http.get(`/v1/secure/data`, { responseType: 'text', headers });
  }
  

}
