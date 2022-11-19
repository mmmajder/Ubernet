import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-driver-availability',
  templateUrl: './driver-availability.component.html',
  styleUrls: ['./driver-availability.component.css']
})
export class DriverAvailabilityComponent implements OnInit {
  workingHours: string = "0h";

  constructor() { }

  ngOnInit(): void {
  }

}
