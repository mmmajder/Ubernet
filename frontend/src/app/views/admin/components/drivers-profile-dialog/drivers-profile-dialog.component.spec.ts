import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriversProfileDialogComponent } from './drivers-profile-dialog.component';

describe('DriversProfileDialogComponent', () => {
  let component: DriversProfileDialogComponent;
  let fixture: ComponentFixture<DriversProfileDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriversProfileDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriversProfileDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
