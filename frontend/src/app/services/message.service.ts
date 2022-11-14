import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

class Message {
  clientEmail!: string;
  adminEmail!: string;
  isSentByAdmin!: boolean;
  content!: string;
  time!: string; // "dd.MM.yyyy. hh:mm"
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
    this.messageUrl = 'http://localhost:8000/messages';
  }

  // for user
  public getMessages(clientEmail:string){
    return this.http.get(this.messageUrl + "/" + clientEmail, this.httpOptions);
  }

  // for admin
  public getMessagesAsAdmin(clientEmail:string){
    return this.http.get(this.messageUrl + "/admin/" + clientEmail, this.httpOptions);
  }

  public getTest() {
    return this.http.get(this.messageUrl + "/test" , this.httpOptions);
  }

  // for admin

}
