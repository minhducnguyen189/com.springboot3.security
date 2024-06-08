import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  constructor(private http: HttpClient) { }


  getSecureMessageAdmin(): Observable<string> {
    const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');
    headers.append('Content-Type', "text/plain");
    return this.http.get("/v1/secure/messages/admin", {headers, responseType: 'text'});
  }

  getSecureMessageUser(): Observable<string> {
    const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');
    headers.append('Content-Type', "text/plain");
    return this.http.post("/v1/secure/messages/user", {}, {headers, responseType: 'text'});
  }


}
