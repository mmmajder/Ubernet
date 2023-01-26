import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {NotificationDTO} from "../../../model/NotificationDTO";
import {RideService} from "../../../services/ride.service";
import {User} from "../../../model/User";
import {Place} from "../../../model/Position";
import {RideDTO} from "../../../model/RideDTO";
import {Store} from "@ngxs/store";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DecrementTokens} from "../../../store/actions/tokens.action";

@Component({
  selector: 'app-ride-split-fare-dialog',
  templateUrl: './ride-split-fare-dialog.component.html',
  styleUrls: ['./ride-split-fare-dialog.component.css']
})
export class RideSplitFareDialogComponent implements OnInit {

  notification: NotificationDTO
  price: number;
  name: string;
  surname: string;
  path: string;
  ride: RideDTO;
  loggedUser: User;

  constructor(private _snackBar: MatSnackBar, private store: Store, @Inject(MAT_DIALOG_DATA) public data: NotificationDTO, private rideService: RideService) {
    this.notification = data;
  }

  ngOnInit(): void {
    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => {
        this.loggedUser = user;
      }
    })
    console.log(this.notification)
    this.rideService.getById(this.notification.rideId).subscribe((ride) => {
      this.ride = ride;
      this.path = this.printCheckpoints(ride.route.checkPoints)
      const sender = ride.customers[0]
      this.price = ride.payment.totalPrice
      this.name = sender.name
      this.surname = sender.surname
    })
  }

  printCheckpoints(checkPoints: Place[]) {
    let res = ""
    checkPoints.forEach((checkPoint, index) => {
      if (index === 0) res += checkPoint.name
      else res += " -> " + checkPoint.name
    })
    return res
  }

  accept() {
    this.ride.payment.customers.forEach((customerPayment) => {
      if (customerPayment.customer.email === this.loggedUser.email) {
        this.rideService.acceptRequestSplitFare(customerPayment.url).subscribe({
          next: () => {
            this.store.dispatch([new DecrementTokens(customerPayment.pricePerCustomer)])
            this._snackBar.open("Successfully accepted split fare request!", '', {
              duration: 3000,
              panelClass: ['snack-bar']
            })
          },
          error: (resp) => {
            this._snackBar.open(resp.error, '', {
              duration: 3000,
              panelClass: ['snack-bar']
            })
          }
        })
      }
    })
  }
}
