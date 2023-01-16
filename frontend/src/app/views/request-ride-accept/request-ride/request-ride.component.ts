import {Component, OnInit} from '@angular/core';
import {RideService} from "../../../services/ride.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-request-ride',
  templateUrl: './request-ride.component.html',
  styleUrls: ['./request-ride.component.css']
})
export class RequestRideComponent implements OnInit {
  response: string;

  constructor(private route: ActivatedRoute, private router: Router, private rideService: RideService, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    let url = this.route.snapshot.paramMap.get('acceptRideUrl')!;
    this.rideService.acceptRequestSplitFair(url).subscribe({
      next: () => {
        this.response = "Successfully accepted payment"
      },
      error: (resp) => {
        console.log(resp)
        this.response = resp.error
      }
    })
  };
}
