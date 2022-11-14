import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";

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

  constructor() {
  }

  ngOnInit(): void {
  }

}
