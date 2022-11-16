import {Component, OnInit} from '@angular/core';
import {RideToRate} from "../../../../model/RideToRate";
import {
  CreateRestaurantDialogComponent
} from "../../../restaurants/components/create-restaurant-dialog/create-restaurant-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {RateDialogComponent} from "../rate-dialog/rate-dialog.component";

@Component({
  selector: 'app-customer-ratings-dashboard',
  templateUrl: './customer-ratings-dashboard.component.html',
  styleUrls: ['./customer-ratings-dashboard.component.css']
})
export class CustomerRatingsDashboardComponent implements OnInit {
  rides: RideToRate[] = [{
    "start": "Kuca",
    "destination": "Poso",
    "time": "15.10. 16:10h",
    "timeToRate": "3 days"
  },
    {
      "start": "Kuca",
      "destination": "Poso",
      "time": "15.10. 16:10h",
      "timeToRate": "3 days"
    },
    {
      "start": "Kuca",
      "destination": "Poso",
      "time": "15.10. 16:10h",
      "timeToRate": "3 days"
    }];

  constructor(private ratingDialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  openRatingsDialog(ride: RideToRate) {
    let dialogRef = this.ratingDialog.open(RateDialogComponent);
    // dialogRef.afterClosed().subscribe(res => {
    //   if (res !== undefined)
    //     this.createRestaurant(res.data)
    // })
  }

}
