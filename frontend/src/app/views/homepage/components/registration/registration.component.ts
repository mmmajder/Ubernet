import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
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

  formGroup = this._formBuilder.group({
    emailFormControl: ['email', [Validators.required, Validators.email]],
    phoneFormControl: ['phoneNumber', [Validators.required]],
    nameFormControl: ['name', [Validators.required]],
    lastNameFormControl: ['lastName', [Validators.required]],
    cityFormControl: ['city', [Validators.required]],
    passwordFormControl: ['password', [Validators.required, Validators.minLength(6)]],
    password2FormControl: ['password2', [Validators.required]]
  });

  email = "";
  phoneNumber = "";
  password = "";
  password2 = "";
  name = "";
  lastName = "";
  city = "";

  hide = true;
  hide2 = true;

  constructor(private _snackBar: MatSnackBar, private _formBuilder: FormBuilder, private authService: AuthService) {
  }

  registerNewUser() {
    if (this.password !== this.password2) {
      this.openSnackBar("Passwords are not the same.");
    } else {
      const requestBody: RegisterCredentials = {
        "email": this.email,
        "password": this.password,
        "name": this.name,
        "surname": this.lastName,
        "phoneNumber": this.phoneNumber,
        "city": this.city,
      }

      this.authService.register(requestBody).subscribe({
        next: () => this.openSnackBar("We sent you registration link"),
        error: (message) => this.openSnackBar(message)
      });
    }
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }

  switchToLoginForm() {
    this.switchForm.emit();
  }
}
