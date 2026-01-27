import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'private-app';

  constructor() {
    if (window.opener && window.name === 'refreshtokenpopup') {
      window.opener.postMessage('refresh-success', window.location.origin);
      // window.close();
    }
  }
}
