import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-refresh-action-dialog',
  templateUrl: './refresh-action-dialog.component.html',
  styleUrls: ['./refresh-action-dialog.component.scss']
})
export class RefreshActionDialogComponent implements OnInit, OnDestroy {

  private _messageHandler: (event: MessageEvent) => void;

  constructor(public dialogRef: MatDialogRef<RefreshActionDialogComponent>) {
    this._messageHandler = (event: MessageEvent) => {
      if (event.origin === window.location.origin && event.data === 'refresh-success') {
        this.dialogRef.close(true);
      }
    };
  }

  ngOnInit(): void {
    const url = window.location.origin + '/api/private-app/actions/refresh-token';
    const popup = window.open(url, 'refreshtokenpopup', 'width=500,height=600');
    
    if (popup) {
        window.addEventListener('message', this._messageHandler);
    } else {
        console.error('Popup blocked. Please allow popups for this site.');
        // Fallback or user instruction could be added here
        this.dialogRef.close(false);
    }
  }

  ngOnDestroy(): void {
    window.removeEventListener('message', this._messageHandler);
  }

}