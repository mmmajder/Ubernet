import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {UserService} from "../../../services/user.service";
import {AuthService} from "../../../services/auth.service";
import {PasswordChangeInfo} from "../../../model/PasswordChangeInfo";
import {User} from "../../../model/User";

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


  constructor(private userService: UserService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      //TODO should the loggedUser be taken like this? sometimes because of the async it's not working well
      this.loggedUser = data;
    });
  }

  changePassword(): void {
    if (this.loggedUser === null || this.currentPassword === "" || this.newPassword === "" || this.newPassword !== this.reEnteredNewPassword)
    {
      console.log("something is not filled")
      //TODO induce showing of errors on form fields
      return
    }

    const passwordChangeInfo: PasswordChangeInfo = new PasswordChangeInfo(this.currentPassword, this.newPassword, this.reEnteredNewPassword);
    this.userService.changePassword(this.loggedUser.email, passwordChangeInfo)
      .subscribe((data) => {
        // console.log(data);
      });
  }
}
