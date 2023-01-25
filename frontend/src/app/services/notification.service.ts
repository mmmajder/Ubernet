import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {NotificationDTO} from "../model/NotificationDTO";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private readonly notificationUrl: string;

  constructor(private http: HttpClient) {
    this.notificationUrl = 'http://localhost:8000/notification';
  }

  public getNotifications(customerEmail: string): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(this.notificationUrl + "/" + customerEmail, NotificationService.getHttpOptions());
  }

  public getNotificationById(id: number): Observable<NotificationDTO> {
    return this.http.get<NotificationDTO>(this.notificationUrl + "/by-id/" + id, NotificationService.getHttpOptions());
  }

  public areNotificationSeen(email: string): Observable<boolean> {
    return this.http.get<boolean>(this.notificationUrl + "/is-opened/" + email, NotificationService.getHttpOptions());
  }

  public openNotificationForCustomer(email: string): Observable<NotificationDTO[]> {
    return this.http.put<NotificationDTO[]>(this.notificationUrl + "/open/" + email, NotificationService.getHttpOptions());
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
