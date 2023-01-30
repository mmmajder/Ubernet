import {Component} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {CarTypeService} from "../../../../services/car-type.service";
import {AuthService} from "../../../../services/auth.service";
import {CarService} from "../../../../services/car.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CurrentlyLogged} from "../../../../store/actions/loggedUser.actions";
import {Store} from "@ngxs/store";
import {DriversService} from "../../../../services/drivers.service";
import {DriverChangeRequest} from "../../../../model/RegisterCredentials";

@Component({
  selector: 'app-driver-request-change',
  templateUrl: './driver-request-change.component.html',
  styleUrls: ['./driver-request-change.component.css']
})
export class DriverRequestChangeComponent {
  formGroup = this._formBuilder.group({
    emailFormCtr: ['', [Validators.required, Validators.email]],
    phoneFormCtr: ['', Validators.required],
    nameFormCtr: ['', Validators.required],
    lastNameFormCtr: ['', Validators.required],
    cityFormCtr: ['', Validators.required],
    platesFormCtr: ['', Validators.required],
    carNameFormCtr: ['', Validators.required],
  });

  email = "";
  phoneNumber = "";
  name = "";
  lastName = "";
  city = "";

  allowsBaby = false;
  allowsPet = false;
  canRegister = false;
  selectedCarType = "";
  carTypes: string[] = [];
  plates: string;
  indexOfCarType = 0;
  carName = "";
  alreadyRequestedChanges = true;

  driver: DriverChangeRequest;
  driverEmail = '';

  constructor(private _snackBar: MatSnackBar, private store: Store, private driverService: DriversService, private _formBuilder: FormBuilder, private carTypeService: CarTypeService, private authService: AuthService, private carService: CarService) {
    this.carTypeService.getCarTypes()
      .subscribe(types => {
        for (const type of types)
          this.carTypes.push(type.name);
      });
    this.store.dispatch(new CurrentlyLogged()).subscribe(
      (resp) => {
        this.driverEmail = resp.loggedUser.email;
        this.loadDriverData(resp.loggedUser.email);
      }
    );
  }

  loadDriverData(driverEmail: string) {
    this.driverService.getFullDriver(driverEmail).subscribe((driver: DriverChangeRequest) => {
      this.driver = driver;
      this.email = driverEmail;
      this.name = driver.name;
      this.lastName = driver.surname;
      this.allowsBaby = driver.allowsBabies;
      this.allowsPet = driver.allowsPets;
      this.carName = driver.carName;
      this.selectedCarType = driver.carType;
      this.plates = driver.plates;
      this.city = driver.city;
      this.phoneNumber = driver.phoneNumber;
      this.alreadyRequestedChanges = driver.alreadyRequestedChanges;
    })
  }

  anythingChanged(): boolean {
    if (this.name !== this.driver.name)
      return true;
    if (this.lastName !== this.driver.surname)
      return true;
    if (this.phoneNumber !== this.driver.phoneNumber)
      return true;
    if (this.city !== this.driver.city)
      return true;
    if (this.allowsBaby !== this.driver.allowsBabies)
      return true;
    if (this.allowsPet !== this.driver.allowsPets)
      return true;
    if (this.selectedCarType !== this.driver.carType)
      return true;
    if (this.plates !== this.driver.plates)
      return true;
    return this.carName !== this.driver.carName;
  }

  requestChanges() {
    if (this.anythingChanged()) {
      this.driverService.requestProfileChanges({
        "email": this.email,
        "name": this.name,
        "surname": this.lastName,
        "phoneNumber": this.phoneNumber,
        "city": this.city,
        "allowsPets": this.allowsPet,
        "allowsBabies": this.allowsBaby,
        "carName": this.carName,
        "carType": this.selectedCarType,
        "plates": this.plates,
        "alreadyRequestedChanges": this.alreadyRequestedChanges
      }).subscribe({
        next: (success: boolean) => {
          if (success) {
            this.snack("Successfully requested changes.");
            this.alreadyRequestedChanges = true;
          } else {
            this.snack("Error occured.");
          }
        },
        error: () => this.snack("Error occured.")
      })
    } else {
      this.snack("You have to change something.")
    }
  }

  snack(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    });
  }
}
