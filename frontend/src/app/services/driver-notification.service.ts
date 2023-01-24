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

  // public getNotificationById(id: number): Observable<NotificationDTO> {
  //   return this.http.get<NotificationDTO>(this.notificationUrl + "/by-id/" + id, NotificationService.getHttpOptions());
  // }
  //
  // public areNotificationSeen(email: string): Observable<boolean> {
  //   return this.http.get<boolean>(this.notificationUrl + "/is-opened/" + email, NotificationService.getHttpOptions());
  // }
  //
  // public openNotificationForCustomer(email: string): Observable<void> {
  //   return this.http.put<void>(this.notificationUrl + "/open/" + email, NotificationService.getHttpOptions());
  // }


}
