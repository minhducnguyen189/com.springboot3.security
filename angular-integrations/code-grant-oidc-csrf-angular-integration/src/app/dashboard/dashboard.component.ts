import { Component } from '@angular/core';
import { MessagesService } from '../service/messages.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  adminMessage: string = '';
  userMessage: string = '';

  constructor(private messageService: MessagesService){}

 getSecureMessages() {
    this.messageService.getSecureMessageUser()
    .subscribe(message => this.userMessage = message);
    this.messageService.getSecureMessageAdmin()
    .subscribe(message => this.adminMessage = message);
 }

}
