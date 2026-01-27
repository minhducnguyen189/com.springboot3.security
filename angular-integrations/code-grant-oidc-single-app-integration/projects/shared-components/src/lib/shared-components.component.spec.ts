import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedComponentsComponent } from './shared-components.component';

describe('SharedComponentsComponent', () => {
  let component: SharedComponentsComponent;
  let fixture: ComponentFixture<SharedComponentsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SharedComponentsComponent]
    });
    fixture = TestBed.createComponent(SharedComponentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
