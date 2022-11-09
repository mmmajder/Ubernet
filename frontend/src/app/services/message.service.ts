import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

class Message {
  clientEmail!: string;
  adminEmail!: string;
  isSentByAdmin!: boolean;
  content!: string;
  time!: string; // "dd.MM.yyyy. hh:mm"

  private String clientEmail;
  private String adminEmail;
  private boolean isSentByAdmin;
  private String content;
  private LocalDateTime time;
}

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private readonly messageUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.messageUrl = 'http://localhost:8000/chat';
  }

  // for user
  public getUserMessages(): Observable<Message> {
    return this.http.get<ActiveCarResponse>(this.mapUrl + "/active", this.httpOptions);
  }

  // for admin

}
