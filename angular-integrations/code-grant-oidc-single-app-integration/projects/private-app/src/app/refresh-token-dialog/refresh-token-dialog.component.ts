import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RefreshActionDialogComponent } from './refresh-action-dialog/refresh-action-dialog.component';
import { ApiError } from '../interceptor/auth-interceptor';

@Component({
  selector: 'app-refresh-token-dialog',
  templateUrl: './refresh-token-dialog.component.html',
  styleUrls: ['./refresh-token-dialog.component.scss']
})
export class RefreshTokenDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<RefreshTokenDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ApiError,
    private router: Router,
    private dialog: MatDialog
  ) { }


  logout(): void {
    this.dialogRef.close();
    // Assuming standard login route
    this.router.navigate(['/login']);
  }

  refreshToken(): void {
    const actionDialog = this.dialog.open(RefreshActionDialogComponent, {
      disableClose: true,
      width: '300px'
    });
    
    this.dialogRef.close();

    actionDialog.afterClosed().subscribe(success => {
      if (success) {
        window.location.reload();
      }
    });
  }
}