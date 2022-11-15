import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  hideCurrentPassword: boolean = true;
  hideNewPassword: boolean = true;
  hideReEnteredNewPassword: boolean = true;

  currentPassword: any;
  newPassword: any;
  reEnteredNewPassword: any;

  currentPasswordFormControl = new FormControl('', [Validators.required]);
  newPasswordFormControl = new FormControl('', [Validators.required, Validators.minLength(6)]);
  reEnteredNewPasswordFormControl = new FormControl('', [Validators.required]);

  constructor(private userService: UserService) {}

  ngOnInit(): void {
  }

  changePassword(): void {
    if (this.currentPassword !== "" && this.newPassword !== "" && this.newPassword === this.reEnteredNewPassword)
    {
      this.userService.changePassword("admin@gmail.com", "admin", "admin222")
        .subscribe((data) => {
          console.log(data);
        });
    }
  }
}
