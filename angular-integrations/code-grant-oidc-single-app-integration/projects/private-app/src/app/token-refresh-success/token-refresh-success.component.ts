import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-token-refresh-success',
  templateUrl: './token-refresh-success.component.html',
  styleUrls: ['./token-refresh-success.component.scss']
})
export class TokenRefreshSuccessComponent implements OnInit, OnDestroy {

  countdown: number = 5;
  private _timer: any;

  constructor() { }

  ngOnInit(): void {
    this.startCountdown();
  }

  startCountdown(): void {
    this._timer = setInterval(() => {
      this.countdown--;
      if (this.countdown <= 0) {
        this.closeImmediately();
      }
    }, 1000);
  }

  closeImmediately(): void {
    if (this._timer) {
      clearInterval(this._timer);
    }
    if (window.opener) {
      window.opener.postMessage('refresh-success', window.location.origin);
    }
    window.close();
  }

  ngOnDestroy(): void {
    if (this._timer) {
      clearInterval(this._timer);
    }
  }

}