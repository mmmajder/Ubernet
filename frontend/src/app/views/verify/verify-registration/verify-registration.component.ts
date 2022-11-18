import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ActivatedRoute} from '@angular/router';
import {Login, Verify} from "../../../store/actions/authentication.actions";
import {Store} from "@ngxs/store";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-verify-registration',
  templateUrl: './verify-registration.component.html',
  styleUrls: ['./verify-registration.component.css']
})
export class VerifyRegistrationComponent implements OnInit {

  verificationCode: string | null

  constructor(private _snackBar: MatSnackBar, private router: Router, private route: ActivatedRoute, private store: Store) {
  }

  ngOnInit(): void {
    this.verificationCode = this.route.snapshot.paramMap.get('verificationCode');
    this.store.dispatch(new Verify({
      "verificationCode": this.verificationCode as string
    })).subscribe({
      next: () => this._snackBar.open("Account is verified", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      }),
      error: () => this._snackBar.open("Wrong email or password.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    });
  }

  navigate() {
    this.router.navigate(['/']);
  }

}
