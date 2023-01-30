import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ActivatedRoute} from '@angular/router';
import {Store} from "@ngxs/store";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {SetPasswordDTO} from "../../../../model/SetPasswordDTO";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordCode: string

  hideNewPassword = true;
  hideReEnteredNewPassword = true;

  newPassword = "";
  reEnteredNewPassword = "";

  newPasswordFormControl = new FormControl('', [Validators.required, Validators.minLength(6)]);
  reEnteredNewPasswordFormControl = new FormControl('', [Validators.required]);

  constructor(private authService: AuthService, private _snackBar: MatSnackBar, private router: Router, private route: ActivatedRoute, private store: Store) {
  }

  ngOnInit(): void {
    this.resetPasswordCode = this.route.snapshot.paramMap.get('resetPasswordCode') as string;
  }

  changePassword() {
    if (this.newPassword === "" || this.newPassword !== this.reEnteredNewPassword) {
      this._snackBar.open("Please enter valid email", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
      return
    }
    const setPasswordDTO = new SetPasswordDTO();
    setPasswordDTO.newPassword = this.newPassword
    setPasswordDTO.reEnteredNewPassword = this.reEnteredNewPassword
    this.authService.setPassword(this.resetPasswordCode, setPasswordDTO)
      .subscribe({
        next: () => {
          this._snackBar.open("Password set successfully.", '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
          this.router.navigate(['']);
        },
        error: () => {
          this._snackBar.open("Please enter valid email", '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
        }
      })
  }

}
