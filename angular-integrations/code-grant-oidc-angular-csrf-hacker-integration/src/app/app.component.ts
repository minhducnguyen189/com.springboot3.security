import { Component, OnInit } from '@angular/core';
import { MessagesService } from './service/messages.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  adminMessage: string = '';
  userMessage: string = '';

  constructor(private messageService: MessagesService){};

  ngOnInit(): void {
    this.messageService.getSecureMessageUser()
    .subscribe(message => this.userMessage = message);

    this.messageService.getSecureMessageAdmin()
    .subscribe(message => this.adminMessage = message);
  }


}
