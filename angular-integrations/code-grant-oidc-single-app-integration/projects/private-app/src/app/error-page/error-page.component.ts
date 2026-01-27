import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.scss']
})
export class ErrorPageComponent {
  errorCode: string = '';
  errorMessage: string = '';

  constructor(private route: ActivatedRoute) {
    this.route.queryParams.subscribe(params => {
      this.errorCode = params['code'] || 'Error';
      this.errorMessage = params['message'] || 'An unexpected error occurred.';
    });
  }
}
