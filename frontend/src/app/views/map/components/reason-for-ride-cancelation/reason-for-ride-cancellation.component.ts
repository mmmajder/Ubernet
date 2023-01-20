import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {RideDetails} from "../../../../model/RideDetails";
import {CancelRideRequest} from "../../../../model/CancelRideRequest";
import {RideDenialService} from "../../../../services/ride-denial.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-reason-for-ride-cancellation',
  templateUrl: './reason-for-ride-cancellation.component.html',
  styleUrls: ['./reason-for-ride-cancellation.component.css']
})
export class ReasonForRideCancellationComponent implements OnInit {

  ride: RideDetails;
  shouldSetDriverInactive: boolean;
  reason: string;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private rideDenialService: RideDenialService, private _snackBar: MatSnackBar) {
    this.ride = data.ride;
    this.shouldSetDriverInactive = data.shouldSetDriverInactive;
  }

  ngOnInit(): void {
  }

  cancelRide() {
    let cancelRideRequest = new CancelRideRequest();
    cancelRideRequest.reason = this.reason;
    cancelRideRequest.shouldSetDriverInactive = this.shouldSetDriverInactive;
    this.rideDenialService.createRideDenial(cancelRideRequest, this.ride.id).subscribe({
      next: () => {
        if (cancelRideRequest.shouldSetDriverInactive)
          this._snackBar.open("We hope everyone is safe and sound!", '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
        else
          this._snackBar.open("Thank you for response! You can continue driving for other passengers.", '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
      },
      error: () => this._snackBar.open("Error occurred while updating data.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    })
  }
}
