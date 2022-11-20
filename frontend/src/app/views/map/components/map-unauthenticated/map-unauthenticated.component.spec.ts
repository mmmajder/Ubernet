import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapUnauthenticatedComponent } from './map-unauthenticated.component';

describe('MapUnauthenticatedComponent', () => {
  let component: MapUnauthenticatedComponent;
  let fixture: ComponentFixture<MapUnauthenticatedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MapUnauthenticatedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MapUnauthenticatedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
