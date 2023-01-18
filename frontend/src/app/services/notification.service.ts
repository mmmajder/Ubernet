import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {Car} from "../model/Car";
import {ActiveCarResponse} from "../model/ActiveCarResponse";
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