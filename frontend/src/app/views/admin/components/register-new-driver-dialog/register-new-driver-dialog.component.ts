import {Component} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {CarTypeService} from "../../../../services/car-type.service";
import {AuthService} from "../../../../services/auth.service";
import {MatSnackBar} from "@angular/material/snack-bar";

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

  email: string = "";
  phoneNumber: string = "";
  password: string = "";
  password2: string = "";
  name: string = "";
  lastName: string = "";
  city: string = "";

  hide: boolean = true;
  hide2: boolean = true;
  allowsBaby: boolean = false;
  allowsPet: boolean = false;
  canRegister: boolean = false;
  selectedCarType: string = "";
  carTypes: string[] = [];
  plates: string;
  indexOfCarType: number = 0;
  carName: string = "";

  constructor(private _snackBar: MatSnackBar, private _formBuilder: FormBuilder, private authService: AuthService, private carTypeService: CarTypeService) {
    this.carTypeService.getCarTypes()
      .subscribe(types => {
        for(let type of types)
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
      next: () => this._snackBar.open("Driver added successfully.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      }),
      error: () => this._snackBar.open("Error occurred.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    });
  }
}
