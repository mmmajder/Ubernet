import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {UserService} from "../../../services/user.service";
import {AuthService} from "../../../services/auth.service";
import {PasswordChangeInfo} from "../../../model/PasswordChangeInfo";
import {User} from "../../../model/User";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  hideCurrentPassword = true;
  hideNewPassword = true;
  hideReEnteredNewPassword = true;

  currentPassword = "";
  newPassword = "";
  reEnteredNewPassword = "";
  loggedUser: User;

  currentPasswordFormControl = new FormControl('', [Validators.required]);
  newPasswordFormControl = new FormControl('', [Validators.required, Validators.minLength(6)]);
  reEnteredNewPasswordFormControl = new FormControl('', [Validators.required]);


  constructor(private userService: UserService, private authService: AuthService, private _snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      this.loggedUser = data;
    });
  }

  changePassword(): void {
    if (this.loggedUser === null || this.currentPassword === "" || this.newPassword === "" || this.newPassword !== this.reEnteredNewPassword)
    { return}

    const passwordChangeInfo: PasswordChangeInfo = new PasswordChangeInfo(this.currentPassword, this.newPassword, this.reEnteredNewPassword);
    this.userService.changePassword(this.loggedUser.email, passwordChangeInfo)
      .subscribe({
        next: () => this.openSnackBar("Password changed."),
        error: () => this.openSnackBar("Error occured.")
      });
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }
}
