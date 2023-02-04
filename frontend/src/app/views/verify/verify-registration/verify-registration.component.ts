import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ActivatedRoute} from '@angular/router';
import {Store} from "@ngxs/store";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-verify-registration',
  templateUrl: './verify-registration.component.html',
  styleUrls: ['./verify-registration.component.css']
})
export class VerifyRegistrationComponent implements OnInit {
  header: string
  message: string
  constructor(private authService: AuthService, private _snackBar: MatSnackBar, private router: Router, private route: ActivatedRoute, private store: Store) {
  }

  ngOnInit(): void {
    const verificationCode = this.route.snapshot.paramMap.get('verificationCode') as string;
    this.authService.verify(verificationCode).subscribe({
      next: () => {
        this.header = "Congratulations";
        this.message = "You have successfully verified your profile";
      },
      error: (res) => {
        this.header = "Unfortunately";
        this.message = res.error;
      }
    });
  }

  navigate() {
    this.router.navigate(['/']);
  }

}
