import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {CarTypeGetResponse} from "../model/CarTypeGetResponse";
import {CreditCard} from "../model/CreditCard";
import {AuthService} from "./auth.service";
import {Car} from "../model/Car";

@Injectable({
  providedIn: 'root'
})
export class CarService {

  private readonly carUrl: string;

  constructor(private http: HttpClient) {
    this.carUrl = 'http://localhost:8000/car';
  }

  public putCar(car: Car): Observable<Car> {
    return this.http.put<Car>(this.carUrl + "/update", car, CarService.getHttpOptions());
  }

  public getCar(driverEmail: string): Observable<Car> {
    return this.http.get<Car>(this.carUrl + "/driver/" + driverEmail, CarService.getHttpOptions());
  }

  public static getHttpOptions() {
    console.log(localStorage.getItem('token'))
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
      })
    };
  }
}
