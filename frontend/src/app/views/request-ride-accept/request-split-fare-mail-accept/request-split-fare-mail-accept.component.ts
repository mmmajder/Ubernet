import {Component, OnInit} from '@angular/core';
import {RideService} from "../../../services/ride.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-request-ride',
  templateUrl: './request-split-fare-mail-accept.component.html',
  styleUrls: ['./request-split-fare-mail-accept.component.css']
})
export class RequestSplitFareMailAcceptComponent implements OnInit {
  response: string;

  constructor(private route: ActivatedRoute, private router: Router, private rideService: RideService, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    const url = this.route.snapshot.paramMap.get('acceptRideUrl')!;
    this.rideService.acceptRequestSplitFare(url).subscribe({
      next: () => {
        this.response = "Successfully accepted payment"
      },
      error: (resp) => {
        this.response = resp.error
      }
    })
  }
}
