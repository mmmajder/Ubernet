import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {ActiveCarResponse} from "../model/ActiveCarResponse";
import * as L from "leaflet";
import {CarTypeService} from "./car-type.service";
import {CarTypeGetResponse} from "../model/CarTypeGetResponse";

@Injectable({
  providedIn: 'root'
})
export class RidePayService {

  constructor(private carTypeService: CarTypeService) {
  }

  calculateRidePrice(typeOfVehicleName: string, lengthInKm: number): number {
    this.carTypeService.getCarType(typeOfVehicleName).subscribe((typeOfVehicle:CarTypeGetResponse) => {
      return 1 * lengthInKm * 120
    })
    return 0
  }
}
