import {Component} from '@angular/core';
import {RideToRate} from "../../../../model/RideToRate";
import {MatDialog} from "@angular/material/dialog";
import {RateDialogComponent} from "../rate-dialog/rate-dialog.component";
import {CurrentlyLogged} from "../../../../store/actions/loggedUser.actions";
import {Store} from "@ngxs/store";
import {RatingsService} from "../../../../services/ratings.service";
import {RideDetailsDialogComponent} from "../../../../shared/ride-details-dialog/ride-details-dialog.component";

@Component({
  selector: 'app-customer-ratings-dashboard',
  templateUrl: './customer-ratings-dashboard.component.html',
  styleUrls: ['./customer-ratings-dashboard.component.css']
})
export class CustomerRatingsDashboardComponent {
  rides: RideToRate[] = [];
  customerEmail = "";

  constructor(private dialog: MatDialog, private store: Store, private service: RatingsService) {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.customerEmail = resp.loggedUser.email;
        this.loadRidesToRate();
      }
    });
  }

  openRatingsDialog(ride: RideToRate) {
    const dialogRef = this.dialog.open(RateDialogComponent, {
      height: '350px',
      width: '500px'
    });
    dialogRef.componentInstance.rideId = ride.rideId;
    dialogRef.componentInstance.customerEmail = this.customerEmail;
    dialogRef.afterClosed().subscribe(() => this.loadRidesToRate())
  }

  private loadRidesToRate() {
    this.service.getRidesToRate(this.customerEmail).subscribe({
      next: value => this.rides = value,
      error: err => console.log(err)
    })
  }

  openRideDetails(rideId: number) {
    const dialogRef = this.dialog.open(RideDetailsDialogComponent, {
      height: '600px',
      width: '1000px'
    });
    dialogRef.componentInstance.id = rideId;
    dialogRef.componentInstance.dialogRef = dialogRef;
  }
}
