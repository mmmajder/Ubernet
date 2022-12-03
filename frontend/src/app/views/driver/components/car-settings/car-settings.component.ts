import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {CarTypeGetResponse} from "../../../../model/CarTypeGetResponse";
import {CarTypeService} from "../../../../services/car-type.service";
import {AuthService} from "../../../../services/auth.service";
import {User} from "../../../../model/User";
// import {CarService} from "../../../../services/car.service";
import {CarService} from "../../../../services/car.service";
import {Car} from "../../../../model/Car";

@Component({
  selector: 'app-car-settings',
  templateUrl: './car-settings.component.html',
  styleUrls: ['./car-settings.component.css']
})
export class CarSettingsComponent implements OnInit {
  nameFormControl = new FormControl('', [Validators.required]);
  name: string;

  platesFormControl = new FormControl('', [Validators.required]);
  plates: string;

  allowsBaby: boolean;
  allowsPet: boolean;

  carTypeFormControl = new FormControl('', [Validators.required]);
  selectedCarType: CarTypeGetResponse | undefined;
  carTypes: undefined | CarTypeGetResponse[];

  loggedUser: User | undefined;
  car: Car | undefined;

  isButtonDisabled: boolean;

  indexOfCarType: number;

  constructor(private carTypeService: CarTypeService, private authService: AuthService, private carService: CarService) { }

  ngOnInit(): void {
    this.isButtonDisabled = true;
    this.authService.getCurrentlyLoggedUser().subscribe(driverData => {
      this.loggedUser = driverData;
      this.carService.getCar(driverData.email).subscribe(carData => {
        this.fillCarInfo(carData);

        this.carTypeService.getCarTypes()
          .subscribe(types => {
            this.carTypes = types;
            this.findIndexOfCarType();
            this.fillCarTypeInfo();
          });

        console.log(this.car);
      })
    });
  }

  validateInputs(): boolean {
    if (this.name == "" || this.plates == "" || this.selectedCarType == undefined)
    {
      this.isButtonDisabled = true;
      return false;
    }

    if (this.car != undefined)
      if (this.name == this.car.name && this.plates == this.car.plates && this.selectedCarType == this.car.carType
        && this.allowsBaby == this.car.allowsBaby && this.allowsPet == this.car.allowsPet)
      {
        this.isButtonDisabled = true;
        return false;
      }

    this.isButtonDisabled = false;
    return true;
  }

  fillCarTypeInfo(): void {
    if (this.carTypes){
      this.selectedCarType = this.carTypes[this.indexOfCarType];
    }
  }

  findIndexOfCarType(): number{
    this.indexOfCarType = 0;
    let index: number = 0;

    if (this.car && this.carTypes){
      for (let type of this.carTypes){
        if (type.name === this.car.carType.name){
          this.indexOfCarType = index;

          return this.indexOfCarType;
        }
        index++;
      }
    }

    return this.indexOfCarType;
  }

  fillCarInfo(carData: Car): void{
    // filss all info except car type
    this.car = carData;

    if (this.car != null){
      this.name = this.car.name;
      this.plates = this.car.plates;
      this.allowsBaby = this.car.allowsBaby;
      this.allowsPet = this.car.allowsPet;
    }
  }

  saveChanges() {
    if (!this.validateInputs() || this.car == undefined || this.loggedUser == undefined){
      //TODO throw adequate error | driver has to have a car assigned already because admin creates it
      return;
    }

    this.car.name = this.name;
    this.car.plates = this.plates;
    this.car.allowsBaby = this.allowsBaby;
    this.car.allowsPet = this.allowsPet;

    if (this.selectedCarType != undefined)
      this.car.carType = this.selectedCarType;

    console.log(this.car);
    this.carService.putCar(this.car).subscribe(carData => {
      this.fillCarInfo(carData);
      this.isButtonDisabled = true;
    })
  }


}
