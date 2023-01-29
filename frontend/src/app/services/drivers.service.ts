import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {DriverDTO} from "../model/DriverDTO";
import {DriverChangeRequest, ProfileChangesRequest} from "../model/RegisterCredentials";

@Injectable({
  providedIn: 'root'
})
export class DriversService {

  private readonly driverUrl: string;

  constructor(private http: HttpClient) {
    this.driverUrl = 'http://localhost:8000/driver';
  }

  public getDrivers(): Observable<DriverDTO[]> {
    return this.http.get<DriverDTO[]>(this.driverUrl + "/get-drivers", AuthService.getHttpOptions());
  }

  getDriversEmails(): Observable<string[]> {
    return this.http.get<string[]>(this.driverUrl + "/getDriversEmails", AuthService.getHttpOptions());
  }

  getDriver(email: string): Observable<DriverDTO> {
    return this.http.get<DriverDTO>(this.driverUrl + "/" + email, AuthService.getHttpOptions());
  }

  getFullDriver(email: string): Observable<DriverChangeRequest> {
    return this.http.get<DriverChangeRequest>(this.driverUrl + "/getFullDriver/" + email, AuthService.getHttpOptions());
  }

  toggleActivity(email: string, driverActive: boolean): Observable<void> {
    return this.http.put<void>(this.driverUrl + "/toggle-activity/" + email, driverActive, AuthService.getHttpOptions());
  }

  getNumberOfActiveHoursInLast24h(email: string): Observable<number> {
    return this.http.get<number>(this.driverUrl + "/active-hours/" + email, AuthService.getHttpOptions());
  }

  getProfileChangesRequest(email: string): Observable<ProfileChangesRequest> {
    return this.http.get<ProfileChangesRequest>(this.driverUrl + "/getProfileChangesRequest/" + email, AuthService.getHttpOptions());
  }

  requestProfileChanges(request: DriverChangeRequest): Observable<boolean> {
    return this.http.post<boolean>(this.driverUrl + "/requestProfileChanges", request, AuthService.getHttpOptions());
  }
}
