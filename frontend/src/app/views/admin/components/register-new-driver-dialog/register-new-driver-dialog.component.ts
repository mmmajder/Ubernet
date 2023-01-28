import {Component} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {CarTypeService} from "../../../../services/car-type.service";
import {AuthService} from "../../../../services/auth.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-register-new-driver-dialog',
  templateUrl: './register-new-driver-dialog.component.html',
  styleUrls: ['./register-new-driver-dialog.component.css']
})
export class RegisterNewDriverDialogComponent {
  formGroup = this._formBuilder.group({
    emailFormCtr: ['', [Validators.required, Validators.email]],
    phoneFormCtr: ['', Validators.required],
    nameFormCtr: ['', Validators.required],
    lastNameFormCtr: ['', Validators.required],
    cityFormCtr: ['', Validators.required],
    passwordFormCtr: ['', [Validators.required, Validators.minLength(6)]],
    password2FormCtr: ['', [Validators.required, Validators.minLength(6)]],
    platesFormCtr: ['', Validators.required],
    carNameFormCtr: ['', Validators.required],
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
  allowsBaby = false;
  allowsPet = false;
  canRegister = false;
  selectedCarType = "";
  carTypes: string[] = [];
  plates: string;
  indexOfCarType = 0;
  carName = "";

  constructor(public dialogRef: MatDialogRef<RegisterNewDriverDialogComponent>, private _snackBar: MatSnackBar, private _formBuilder: FormBuilder, private authService: AuthService, private carTypeService: CarTypeService) {
    this.carTypeService.getCarTypes()
      .subscribe(types => {
        for(const type of types)
          this.carTypes.push(type.name);
        this.selectedCarType = this.carTypes[0];
      });
  }

  registerDriver() {
    this.authService.registerDriver({
      "email": this.email,
      "name": this.name,
      "surname": this.lastName,
      "password": this.password,
      "phoneNumber": this.phoneNumber,
      "city": this.city,
      "allowsPets": this.allowsPet,
      "allowsBabies": this.allowsBaby,
      "carName": this.carName,
      "carType": this.selectedCarType,
      "plates": this.plates
    }).subscribe({
      next: () => {
        this._snackBar.open("Driver added successfully.", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      },
      error: () => this._snackBar.open("Error occurred.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    });
  }
}
