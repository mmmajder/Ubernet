import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {MyNotification} from "../shared/navbar/navbar/navbar.component";

export interface NotificationDto {
  message: string
}

@Injectable({
  providedIn: 'root'
})
export class NavbarService {

  private readonly notificationsUrl: string;
  private readonly addNotificationUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.notificationsUrl = 'http://localhost:8081/api/back-office/notifications';
    this.addNotificationUrl = 'http://localhost:8081/api/back-office/notification';
  }

  public findAll(): Observable<NotificationDto[]> {
    return this.http.get<NotificationDto[]>(this.notificationsUrl, this.httpOptions);
  }

  public createNotification(message: MyNotification): Observable<NotificationDto> {
    return this.http.post<NotificationDto>(this.addNotificationUrl, message.message, this.httpOptions);
  }
}
