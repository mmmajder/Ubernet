import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {Driver} from "../model/Driver";

@Injectable({
  providedIn: 'root'
})
export class DriversService {

  private readonly driverUrl: string;

  constructor(private http: HttpClient) {
    this.driverUrl = 'http://localhost:8000/driver';
  }

  public getDrivers(): Observable<Driver[]> {
    return this.http.get<Driver[]>(this.driverUrl + "/get-drivers", AuthService.getHttpOptions());
  }

  getDriversEmails(): Observable<string[]> {
    return this.http.get<string[]>(this.driverUrl + "/getDriversEmails", AuthService.getHttpOptions());
  }

  getDriver(email: string): Observable<Driver> {
    return this.http.get<Driver>(this.driverUrl + "/" + email, AuthService.getHttpOptions());
  }

  toggleActivity(email: string): Observable<void> {
    return this.http.put<void>(this.driverUrl + "/toggle-activity/" + email, AuthService.getHttpOptions());
  }

  getNumberOfActiveHoursInLast24h(email: string): Observable<number> {
    return this.http.get<number>(this.driverUrl + "/active-hours/" + email, AuthService.getHttpOptions());

  }
}
