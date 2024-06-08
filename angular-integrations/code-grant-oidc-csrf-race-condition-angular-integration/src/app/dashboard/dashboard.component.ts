import { Component, OnDestroy } from '@angular/core';
import { MessagesService } from '../service/messages.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnDestroy {

  adminMessage: string = '';
  userMessage: string = '';
  intervals: number[] = [];

  constructor(private messageService: MessagesService){}


  startInterval() {
    const interval = window.setInterval(() => {
      this.messageService.getSecureMessageUser()
      .subscribe(message => this.userMessage = message);
      this.messageService.getSecureMessageUser()
      .subscribe(message => this.userMessage = message);
      this.messageService.getSecureMessageUser()
      .subscribe(message => this.userMessage = message);
      this.messageService.getSecureMessageUser()
      .subscribe(message => this.userMessage = message);
      this.messageService.getSecureMessageUser()
      .subscribe(message => this.userMessage = message);
  
      this.messageService.getSecureMessageAdmin()
      .subscribe(message => this.adminMessage = message);
    }, 500);
    this.intervals.push(interval);
 }

 clearInterval() {
  this.intervals.forEach(interval => window.clearInterval(interval));
 }

  ngOnDestroy(): void {
    this.intervals.forEach(interval => window.clearInterval(interval));
  }

}
