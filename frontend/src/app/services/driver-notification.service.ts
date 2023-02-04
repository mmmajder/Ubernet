import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {DriverNotification} from "../model/DriverNotification";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class DriverNotificationService {
  private readonly notificationUrl: string;

  constructor(private http: HttpClient) {
    this.notificationUrl = 'http://localhost:8000/driver-notification';
  }

  public getCurrent(customerEmail: string): Observable<DriverNotification[]> {
    return this.http.get<DriverNotification[]>(this.notificationUrl + "/" + customerEmail, AuthService.getHttpOptions());
  }
}
