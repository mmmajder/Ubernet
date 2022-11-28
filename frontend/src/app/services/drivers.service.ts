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

}
