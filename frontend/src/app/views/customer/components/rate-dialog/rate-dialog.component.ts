import {Component} from '@angular/core';
import {RatingsService} from "../../../../services/ratings.service";
import {CreateReview} from "../../../../model/Review";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-rate-dialog',
  templateUrl: './rate-dialog.component.html',
  styleUrls: ['./rate-dialog.component.css']
})
export class RateDialogComponent {
  rideId: number;
  customerEmail: string;

  driverRating = 5;
  carRating = 5;
  comment = "";

  constructor(private service: RatingsService, private _snackBar: MatSnackBar) {
  }

  rate() {
    if (this.comment !== "") {
      this.service.rateRide(new CreateReview(this.comment, this.customerEmail, this.carRating, this.driverRating, this.rideId)).subscribe({
        next: () => this._snackBar.open("You rated ride successfully.", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        }),
        error: () => this._snackBar.open("Error occurred.", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      });
    } else {
      this._snackBar.open("Comment cannot be empty.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    }
  }
}
