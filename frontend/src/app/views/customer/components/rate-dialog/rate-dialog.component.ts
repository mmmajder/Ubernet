import { Component, OnInit } from '@angular/core';
import {
  CreateRestaurantDialogComponent
} from "../../../restaurants/components/create-restaurant-dialog/create-restaurant-dialog.component";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-rate-dialog',
  templateUrl: './rate-dialog.component.html',
  styleUrls: ['./rate-dialog.component.css']
})
export class RateDialogComponent implements OnInit {
  driverRatingForm!: FormGroup;
  carRatingForm!: FormGroup;

  driverRating: number = 5;
  carRating: number = 5;

  constructor() { }

  ngOnInit(): void {
  }

  rateCar() {

  }

  rateDriver() {

  }
}
