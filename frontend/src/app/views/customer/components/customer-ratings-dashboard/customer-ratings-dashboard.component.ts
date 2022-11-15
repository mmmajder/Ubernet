import {Component, OnInit} from '@angular/core';
import {RideToRate} from "../../../../model/RideToRate";

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

  constructor() {
  }

  ngOnInit(): void {
  }

}
