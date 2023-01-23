import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {Car} from "../model/Car";
import {ActiveCarResponse} from "../model/ActiveCarResponse";
import {CurrentRide} from "../model/CurrentRide";
import {NavigationDisplay} from "../model/NavigationDisplay";

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

  public getActiveCar(driverEmail: string): Observable<ActiveCarResponse> {
    return this.http.get<ActiveCarResponse>(this.carUrl + "/active-driver/" + driverEmail, CarService.getHttpOptions());
  }

  public findCurrentRideByDriverEmail(email: string): Observable<NavigationDisplay> {
    return this.http.get<NavigationDisplay>(this.carUrl + "/currentRide/" + email, CarService.getHttpOptions());
  }

  public static getHttpOptions() {
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
      })
    };
  }
}
