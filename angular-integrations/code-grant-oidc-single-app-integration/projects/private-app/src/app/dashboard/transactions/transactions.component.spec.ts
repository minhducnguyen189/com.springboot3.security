import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransactionsComponent } from './transactions.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SharedComponentsModule } from 'shared-components';
import { MatButtonModule } from '@angular/material/button';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

describe('TransactionsComponent', () => {
  let component: TransactionsComponent;
  let fixture: ComponentFixture<TransactionsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransactionsComponent],
      imports: [
        HttpClientTestingModule,
        SharedComponentsModule,
        MatButtonModule,
        MatSlideToggleModule
      ]
    });
    fixture = TestBed.createComponent(TransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});