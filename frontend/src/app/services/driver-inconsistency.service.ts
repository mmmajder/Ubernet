import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {DriverInconsistency} from "../model/DriverInconsistency";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class DriverInconsistencyService {
  private readonly driverInconsistencyUrl: string;

  constructor(private http: HttpClient) {
    this.driverInconsistencyUrl = 'http://localhost:8000/driver-inconsistency';
  }

  public create(customerEmail: string, rideId: number): Observable<DriverInconsistency> {
    return this.http.post<DriverInconsistency>(this.driverInconsistencyUrl + "/" + customerEmail, rideId, AuthService.getHttpOptions());
  }
}
