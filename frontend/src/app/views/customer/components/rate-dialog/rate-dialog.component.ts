import {Component} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-rate-dialog',
  templateUrl: './rate-dialog.component.html',
  styleUrls: ['./rate-dialog.component.css']
})
export class RateDialogComponent {
  driverRatingForm!: FormGroup;
  carRatingForm!: FormGroup;

  driverRating = 5;
  carRating = 5;

  rateCar() {
    // TODO
  }

  rateDriver() {
    // TODO
  }
}
