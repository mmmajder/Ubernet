import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthService} from "../../../../services/auth.service";
import {RegisterCredentials} from "../../../../model/RegisterCredentials";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  @Output() switchForm = new EventEmitter();

  email = "";
  phoneNumber = "";
  password = "";
  password2 = "";
  name = "";
  lastName = "";
  city = "";

  hide = true;
  hide2 = true;

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  phoneFormControl = new FormControl('', [Validators.required]);
  nameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  cityFormControl = new FormControl('', [Validators.required]);
  passwordFormControl = new FormControl('', [Validators.required, Validators.minLength(6)]);
  password2FormControl = new FormControl('', [Validators.required]);

  constructor(private _snackBar: MatSnackBar, private authService: AuthService) {
  }

  registerNewUser() {
    const requestBody: RegisterCredentials = {
      "email": this.email,
      "password": this.password,
      "name": this.name,
      "surname": this.lastName,
      "phoneNumber": this.phoneNumber,
      "city": this.city,
    }
    this.authService.register(requestBody).subscribe({
      next: () => {
        this._snackBar.open("We sent you registration link", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      },
      error: (message) => {
        console.log(message)
        this._snackBar.open(message.error, '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      }
    });
  }

  switchToLoginForm() {
    this.switchForm.emit();
  }
}
